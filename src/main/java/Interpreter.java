import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Interprete de Lua 5.4 basado en el arbol sintactico de ANTLR (patron visitor).
 *
 * - Las expresiones (visitXxxExp, visitXxxPrefix) devuelven un unico valor de Lua.
 * - evalMulti / evalExpList devuelven todos los valores (llamadas y '...').
 * - Las sentencias (visitXxxStat) devuelven una "senal": null (normal), BREAK,
 *   GotoSignal, TailCall u Object[] (valores de un return).
 */
public final class Interpreter extends LuaGrammarBaseVisitor<Object> {

    static final String CHUNK = "stdin";
    static final int MAX_DEPTH = 195000;

    final LuaTable globals = new LuaTable();
    Scope scope = null;
    CallState state = new CallState();
    Coroutine currentCoroutine = null;
    LuaFunction nextFunction; // la funcion 'next' (para acelerar pairs)

    // ------------------------------------------------------------------ estructuras

    static final class Ref {
        Object v;

        Ref(Object v) {
            this.v = v;
        }
    }

    /** Ambito lexico. fnBoundary marca el ambito raiz de una funcion (para distinguir upvalues). */
    static final class Scope {
        final Scope parent;
        final boolean fnBoundary;
        Object[] varargs;
        private String[] names;
        private Ref[] refs;
        private int n;
        java.util.ArrayList<Object> toClose; // valores de variables <close>, en orden de declaracion

        /** Valores actuales de las variables de este ambito (para el recolector de basura). */
        void collectValues(java.util.List<Object> out) {
            for (int i = 0; i < n; i++) out.add(refs[i].v);
            if (varargs != null) out.addAll(Arrays.asList(varargs));
            if (toClose != null) out.addAll(toClose);
        }

        Scope(Scope parent, boolean fnBoundary) {
            this.parent = parent;
            this.fnBoundary = fnBoundary;
        }

        Ref find(String name) {
            for (int i = n - 1; i >= 0; i--) {
                if (names[i].equals(name)) return refs[i];
            }
            return null;
        }

        Ref declare(String name, Object v) {
            Ref r = new Ref(v);
            for (int i = 0; i < n; i++) {
                if (names[i].equals(name)) {
                    refs[i] = r; // redeclaracion en el mismo bloque: la nueva variable oculta a la anterior
                    return r;
                }
            }
            if (names == null) {
                names = new String[4];
                refs = new Ref[4];
            } else if (n == names.length) {
                names = Arrays.copyOf(names, n * 2);
                refs = Arrays.copyOf(refs, n * 2);
            }
            names[n] = name;
            refs[n++] = r;
            return r;
        }
    }

    /** Pila de lineas de llamada (para error(msg, nivel) y mensajes de error). */
    static final class CallState {
        int[] lines = new int[256]; // linea del llamador de cada funcion Lua activa
        Scope[] scopes = new Scope[256]; // ambito del llamador de cada funcion Lua activa
        int depth = 0;
        int line = 0;               // linea que se esta ejecutando en la funcion actual
    }

    static final Object BREAK = new Object();

    static final class GotoSignal {
        final String label;

        GotoSignal(String label) {
            this.label = label;
        }
    }

    static final class TailCall {
        final Object fn;
        final Object[] args;
        final int line;
        final Object desc;

        TailCall(Object fn, Object[] args, int line, Object desc) {
            this.fn = fn;
            this.args = args;
            this.line = line;
            this.desc = desc;
        }
    }

    /** Funcion definida en Lua (closure). */
    final class Closure extends LuaFunction {
        final LuaGrammar.BlockContext body;
        final Scope env;
        final String[] params;
        final boolean vararg;

        Closure(String name, LuaGrammar.BlockContext body, Scope env, String[] params, boolean vararg) {
            super(name);
            this.body = body;
            this.env = env;
            this.params = params;
            this.vararg = vararg;
        }

        @Override
        public Object[] call(Object[] args) {
            Closure c = this;
            Object[] a = args;
            while (true) {
                Object r = c.invoke(a);
                if (r instanceof TailCall) {
                    TailCall tc = (TailCall) r;
                    if (tc.fn instanceof Closure) {
                        c = (Closure) tc.fn;
                        a = tc.args;
                        continue;
                    }
                    return callValue(tc.fn, tc.args, tc.line, tc.desc);
                }
                return (Object[]) r;
            }
        }

        Object invoke(Object[] args) {
            CallState st = state;
            if (st.depth >= MAX_DEPTH) throw new LuaError("stack overflow", true);
            if (st.depth == st.lines.length) {
                st.lines = Arrays.copyOf(st.lines, st.depth * 2);
                st.scopes = Arrays.copyOf(st.scopes, st.depth * 2);
            }
            int callerLine = st.line;
            st.scopes[st.depth] = scope;
            st.lines[st.depth++] = callerLine;
            try {
                return invokeBody(args);
            } finally {
                st.scopes[--st.depth] = null;
                st.line = callerLine;
            }
        }

        private Object invokeBody(Object[] args) {
            Scope s = new Scope(env, true);
            int np = params.length;
            for (int i = 0; i < np; i++) s.declare(params[i], i < args.length ? args[i] : null);
            if (vararg) s.varargs = args.length > np ? Arrays.copyOfRange(args, np, args.length) : Lua.NONE;
            Scope saved = scope;
            scope = s;
            try {
                Object sig = execBlock(body);
                if (sig instanceof Object[] || sig instanceof TailCall) return sig;
                return Lua.NONE;
            } finally {
                scope = saved;
            }
        }
    }

    // ------------------------------------------------------------------ arranque

    public Interpreter() {
        StdLib.install(this);
    }

    /** Ejecuta un chunk (programa principal o load()). */
    public LuaFunction makeChunk(LuaGrammar.ChunkContext chunk, String name) {
        return new Closure(name, chunk.block(), null, new String[0], true);
    }

    // ------------------------------------------------------------------ utilidades

    static String text(TerminalNode t) {
        return t.getSymbol().getText();
    }

    static int line(TerminalNode t) {
        return t.getSymbol().getLine();
    }

    static int line(ParserRuleContext c) {
        return c.getStart().getLine();
    }

    static LuaGrammar.ExpContext left(ParserRuleContext ctx) {
        return (LuaGrammar.ExpContext) ctx.children.get(0);
    }

    static LuaGrammar.ExpContext right(ParserRuleContext ctx) {
        return (LuaGrammar.ExpContext) ctx.children.get(2);
    }

    static int opType(ParserRuleContext ctx) {
        return ((TerminalNode) ctx.getChild(1)).getSymbol().getType();
    }

    static int opLine(ParserRuleContext ctx) {
        return ((TerminalNode) ctx.getChild(1)).getSymbol().getLine();
    }

    static String pos(int line) {
        return CHUNK + ":" + line + ": ";
    }

    /** Agrega "stdin:LINEA:" y el nombre de la variable culpable a un error pendiente. */
    LuaError resolve(LuaError e, int line, ParserRuleContext left, ParserRuleContext right) {
        if (e.needsPosition) {
            String msg = String.valueOf(e.value);
            if (e.culprit == 0 && left != null) msg += describe(left);
            else if (e.culprit == 1 && right != null) msg += describe(right);
            e.value = pos(line) + msg;
            e.needsPosition = false;
        }
        return e;
    }

    /** Descripcion de una expresion para los mensajes de error: " (local 'x')", " (field 'y')", etc. */
    String describe(Object src) {
        if (src instanceof String) return (String) src;
        if (!(src instanceof ParserRuleContext)) return "";
        ParserRuleContext c = (ParserRuleContext) src;
        if (c instanceof LuaGrammar.PrefixExprContext) c = ((LuaGrammar.PrefixExprContext) c).prefixexp();
        if (c instanceof LuaGrammar.NamePrefixContext) {
            return " (" + nameKind(text(((LuaGrammar.NamePrefixContext) c).NAME())) + ")";
        }
        if (c instanceof LuaGrammar.FieldPrefixContext) {
            return " (field '" + text(((LuaGrammar.FieldPrefixContext) c).NAME()) + "')";
        }
        if (c instanceof LuaGrammar.IndexPrefixContext) {
            LuaGrammar.ExpContext k = ((LuaGrammar.IndexPrefixContext) c).exp();
            if (k instanceof LuaGrammar.StringExpContext) {
                return " (field '" + stringConst((LuaGrammar.StringExpContext) k) + "')";
            }
            return "";
        }
        if (c instanceof LuaGrammar.MethodPrefixContext) {
            return " (method '" + text(((LuaGrammar.MethodPrefixContext) c).NAME()) + "')";
        }
        if (c instanceof LuaGrammar.StringExpContext) {
            return " (constant '" + stringConst((LuaGrammar.StringExpContext) c) + "')";
        }
        return "";
    }

    private String nameKind(String name) {
        boolean crossed = false;
        for (Scope s = scope; s != null; s = s.parent) {
            if (s.find(name) != null) return (crossed ? "upvalue '" : "local '") + name + "'";
            if (s.fnBoundary) crossed = true;
        }
        return "global '" + name + "'";
    }

    // ------------------------------------------------------------------ variables

    Ref lookup(String name) {
        for (Scope s = scope; s != null; s = s.parent) {
            Ref r = s.find(name);
            if (r != null) return r;
        }
        return null;
    }

    Object getVar(String name) {
        Ref r = lookup(name);
        if (r != null) return r.v;
        if ("_ENV".equals(name)) return globals;
        if (globals.metatable == null) return globals.get(name);
        return Lua.index(globals, name);
    }

    void setVar(String name, Object v) {
        Ref r = lookup(name);
        if (r != null) {
            r.v = v;
        } else if (globals.metatable == null) {
            globals.put(name, v);
        } else {
            Lua.setIndex(globals, name, v);
        }
    }

    Object[] varargs() {
        for (Scope s = scope; s != null; s = s.parent) {
            if (s.fnBoundary) return s.varargs == null ? Lua.NONE : s.varargs;
        }
        return Lua.NONE;
    }

    // ------------------------------------------------------------------ llamadas

    /** Invoca un valor de Lua registrando la linea de la llamada. */
    Object[] callValue(Object fn, Object[] args, int line, Object desc) {
        CallState st = state;
        int savedLine = st.line;
        st.line = line;
        try {
            if (fn instanceof LuaFunction) return ((LuaFunction) fn).call(args);
            if (Lua.metaOf(fn, "__call") == null) {
                throw new LuaError(pos(line) + "attempt to call a " + Lua.typeName(fn) + " value" + describe(desc));
            }
            return Lua.call(fn, args);
        } catch (LuaError e) {
            if (e.needsPosition && line > 0) {
                e.value = pos(line) + e.value;
                e.needsPosition = false;
            }
            throw e;
        } catch (StackOverflowError so) {
            throw new LuaError(pos(line) + "stack overflow");
        } finally {
            st.line = savedLine;
        }
    }

    Object[] evalArgs(LuaGrammar.ArgsContext a) {
        if (a instanceof LuaGrammar.ListArgsContext) {
            LuaGrammar.ExplistContext el = ((LuaGrammar.ListArgsContext) a).explist();
            return el == null ? Lua.NONE : evalExpList(el);
        }
        if (a instanceof LuaGrammar.TableArgsContext) {
            return new Object[] {buildTable(((LuaGrammar.TableArgsContext) a).tableconstructor())};
        }
        return new Object[] {stringConst(((LuaGrammar.StringArgsContext) a).STRING())};
    }

    Object[] evalCall(LuaGrammar.PrefixexpContext fnCtx, LuaGrammar.ArgsContext argsCtx) {
        Object fn = visit(fnCtx);
        Object[] args = evalArgs(argsCtx);
        return callValue(fn, args, line(argsCtx), fnCtx);
    }

    Object[] evalMethodCall(LuaGrammar.PrefixexpContext objCtx, TerminalNode name, LuaGrammar.ArgsContext argsCtx) {
        Object obj = visit(objCtx);
        String m = text(name);
        Object fn;
        try {
            fn = Lua.index(obj, m);
        } catch (LuaError e) {
            throw resolve(e, line(name), objCtx, null);
        }
        Object[] rest = evalArgs(argsCtx);
        Object[] args = new Object[rest.length + 1];
        args[0] = obj;
        System.arraycopy(rest, 0, args, 1, rest.length);
        return callValue(fn, args, line(argsCtx), " (method '" + m + "')");
    }

    /** Devuelve true si la expresion puede producir varios valores (llamada o '...'). */
    static boolean isMulti(LuaGrammar.ExpContext e) {
        if (e instanceof LuaGrammar.VarargExpContext) return true;
        if (e instanceof LuaGrammar.PrefixExprContext) {
            LuaGrammar.PrefixexpContext p = ((LuaGrammar.PrefixExprContext) e).prefixexp();
            return p instanceof LuaGrammar.CallPrefixContext || p instanceof LuaGrammar.MethodPrefixContext;
        }
        return false;
    }

    Object[] evalMulti(LuaGrammar.ExpContext e) {
        if (e instanceof LuaGrammar.PrefixExprContext) {
            LuaGrammar.PrefixexpContext p = ((LuaGrammar.PrefixExprContext) e).prefixexp();
            if (p instanceof LuaGrammar.CallPrefixContext) {
                LuaGrammar.CallPrefixContext c = (LuaGrammar.CallPrefixContext) p;
                return evalCall(c.prefixexp(), c.args());
            }
            if (p instanceof LuaGrammar.MethodPrefixContext) {
                LuaGrammar.MethodPrefixContext c = (LuaGrammar.MethodPrefixContext) p;
                return evalMethodCall(c.prefixexp(), c.NAME(), c.args());
            }
        } else if (e instanceof LuaGrammar.VarargExpContext) {
            return varargs();
        }
        return new Object[] {visit(e)};
    }

    Object[] evalExpList(LuaGrammar.ExplistContext el) {
        List<ParseTree> kids = el.children;
        int nk = kids.size();
        if (nk == 1) return evalMulti((LuaGrammar.ExpContext) kids.get(0));
        int n = (nk + 1) / 2; // exp (',' exp)*
        LuaGrammar.ExpContext last = (LuaGrammar.ExpContext) kids.get(nk - 1);
        boolean multi = isMulti(last);
        Object[] tail = multi ? null : Lua.NONE;
        Object[] r = new Object[multi ? n - 1 : n];
        for (int i = 0; i < n - 1; i++) r[i] = ((LuaGrammar.ExpContext) kids.get(2 * i)).accept(this);
        if (!multi) {
            r[n - 1] = last.accept(this);
            return r;
        }
        tail = evalMulti(last);
        Object[] res = Arrays.copyOf(r, n - 1 + tail.length);
        System.arraycopy(tail, 0, res, n - 1, tail.length);
        return res;
    }

    // ------------------------------------------------------------------ bloques y sentencias

    /** Ejecuta un bloque y, al salir (normal o por error), cierra sus variables {@code <close>}. */
    Object execBlock(LuaGrammar.BlockContext b) {
        Scope sc = scope;
        Object sig;
        try {
            sig = execStatements(b);
        } catch (LuaError e) {
            if (sc != null && sc.toClose != null) closeVariables(sc, e.value);
            throw e;
        }
        if (sc != null && sc.toClose != null) closeVariables(sc, null);
        return sig;
    }

    /** Llama __close(valor, error) en orden inverso al de declaracion. */
    private void closeVariables(Scope sc, Object err) {
        java.util.ArrayList<Object> list = sc.toClose;
        sc.toClose = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            Object v = list.get(i);
            Object h = Lua.metaOf(v, "__close");
            if (h != null) Lua.call(h, v, err);
        }
    }

    private Object execStatements(LuaGrammar.BlockContext b) {
        List<ParseTree> kids = b.children;
        if (kids == null) return null;
        int n = kids.size();
        CallState st = state;
        for (int i = 0; i < n; i++) {
            ParseTree k = kids.get(i);
            if (k instanceof LuaGrammar.RetstatContext) return evalReturn((LuaGrammar.RetstatContext) k);
            LuaGrammar.StatContext stat = (LuaGrammar.StatContext) k;
            st.line = stat.start.getLine();
            Object sig = stat.accept(this);
            if (sig != null) {
                if (sig instanceof GotoSignal) {
                    int target = findLabel(b.stat(), ((GotoSignal) sig).label);
                    if (target >= 0) {
                        i = target;
                        continue;
                    }
                }
                return sig;
            }
        }
        return null;
    }

    private static int findLabel(List<LuaGrammar.StatContext> stats, String label) {
        for (int i = 0; i < stats.size(); i++) {
            LuaGrammar.StatContext s = stats.get(i);
            if (s instanceof LuaGrammar.LabelStatContext
                    && text(((LuaGrammar.LabelStatContext) s).label().NAME()).equals(label)) {
                return i;
            }
        }
        return -1;
    }

    Object execBlockNewScope(LuaGrammar.BlockContext b) {
        Scope saved = scope;
        scope = new Scope(saved, false);
        try {
            return execBlock(b);
        } finally {
            scope = saved;
        }
    }

    private Object evalReturn(LuaGrammar.RetstatContext rs) {
        LuaGrammar.ExplistContext el = rs.explist();
        if (el == null) return Lua.NONE;
        if (el.children.size() == 1 && el.children.get(0) instanceof LuaGrammar.PrefixExprContext) {
            // llamada en posicion de cola: return f(x)
            LuaGrammar.PrefixexpContext p = ((LuaGrammar.PrefixExprContext) el.children.get(0)).prefixexp();
            if (p instanceof LuaGrammar.CallPrefixContext) {
                LuaGrammar.CallPrefixContext c = (LuaGrammar.CallPrefixContext) p;
                Object fn = c.prefixexp().accept(this);
                Object[] args = evalArgs(c.args());
                return new TailCall(fn, args, line(c.args()), c.prefixexp());
            }
        }
        return evalExpList(el);
    }

    @Override
    public Object visitEmptyStat(LuaGrammar.EmptyStatContext ctx) {
        return null;
    }

    @Override
    public Object visitLabelStat(LuaGrammar.LabelStatContext ctx) {
        return null;
    }

    @Override
    public Object visitBreakStat(LuaGrammar.BreakStatContext ctx) {
        return BREAK;
    }

    @Override
    public Object visitGotoStat(LuaGrammar.GotoStatContext ctx) {
        return new GotoSignal(text(ctx.NAME()));
    }

    @Override
    public Object visitDoStat(LuaGrammar.DoStatContext ctx) {
        return execBlockNewScope(ctx.block());
    }

    @Override
    public Object visitCallStat(LuaGrammar.CallStatContext ctx) {
        LuaGrammar.FunctioncallContext fc = ctx.functioncall();
        if (fc instanceof LuaGrammar.PlainCallContext) {
            LuaGrammar.PlainCallContext c = (LuaGrammar.PlainCallContext) fc;
            evalCall(c.prefixexp(), c.args());
        } else {
            LuaGrammar.MethodCallContext c = (LuaGrammar.MethodCallContext) fc;
            evalMethodCall(c.prefixexp(), c.NAME(), c.args());
        }
        return null;
    }

    @Override
    public Object visitAssignStat(LuaGrammar.AssignStatContext ctx) {
        List<LuaGrammar.VarContext> vars = ctx.varlist().var();
        List<LuaGrammar.ExpContext> exps = ctx.explist().exp();
        if (vars.size() == 1 && exps.size() == 1) {
            LuaGrammar.VarContext v = vars.get(0);
            if (v instanceof LuaGrammar.NameVarContext) {
                setVar(text(((LuaGrammar.NameVarContext) v).NAME()), visit(exps.get(0)));
            } else {
                Object[] target = evalTarget(v);
                assign(v, target[0], target[1], visit(exps.get(0)));
            }
            return null;
        }
        // asignacion multiple: primero se evalua todo y luego se asigna
        Object[][] targets = new Object[vars.size()][];
        for (int i = 0; i < vars.size(); i++) {
            if (!(vars.get(i) instanceof LuaGrammar.NameVarContext)) targets[i] = evalTarget(vars.get(i));
        }
        Object[] vals = evalExpList(ctx.explist());
        for (int i = vars.size() - 1; i >= 0; i--) {
            Object val = i < vals.length ? vals[i] : null;
            LuaGrammar.VarContext v = vars.get(i);
            if (v instanceof LuaGrammar.NameVarContext) {
                setVar(text(((LuaGrammar.NameVarContext) v).NAME()), val);
            } else {
                assign(v, targets[i][0], targets[i][1], val);
            }
        }
        return null;
    }

    private Object[] evalTarget(LuaGrammar.VarContext v) {
        if (v instanceof LuaGrammar.IndexVarContext) {
            LuaGrammar.IndexVarContext iv = (LuaGrammar.IndexVarContext) v;
            return new Object[] {visit(iv.prefixexp()), visit(iv.exp())};
        }
        LuaGrammar.FieldVarContext fv = (LuaGrammar.FieldVarContext) v;
        return new Object[] {visit(fv.prefixexp()), text(fv.NAME())};
    }

    private void assign(LuaGrammar.VarContext v, Object obj, Object key, Object val) {
        if (obj instanceof LuaTable && ((LuaTable) obj).metatable == null) {
            try {
                ((LuaTable) obj).put(key, val);
                return;
            } catch (LuaError e) {
                throw resolve(e, line(v), null, null);
            }
        }
        try {
            Lua.setIndex(obj, key, val);
        } catch (LuaError e) {
            ParserRuleContext p = v instanceof LuaGrammar.IndexVarContext
                    ? ((LuaGrammar.IndexVarContext) v).prefixexp()
                    : ((LuaGrammar.FieldVarContext) v).prefixexp();
            throw resolve(e, line(v), p, null);
        }
    }

    @Override
    public Object visitLocalStat(LuaGrammar.LocalStatContext ctx) {
        LuaGrammar.AttnamelistContext al = ctx.attnamelist();
        List<TerminalNode> names = al.NAME();
        Object[] vals = ctx.explist() == null ? Lua.NONE : evalExpList(ctx.explist());
        for (int i = 0; i < names.size(); i++) {
            Object v = i < vals.length ? vals[i] : null;
            scope.declare(text(names.get(i)), v);
            LuaGrammar.AttribContext at = al.attrib(i);
            if (at.NAME() != null) registerAttrib(text(names.get(i)), text(at.NAME()), v, line(ctx));
        }
        return null;
    }

    private void registerAttrib(String var, String attrib, Object v, int line) {
        if ("const".equals(attrib)) return;
        if (!"close".equals(attrib)) {
            throw new LuaError(pos(line) + "unknown attribute '" + attrib + "'");
        }
        if (v == null || Boolean.FALSE.equals(v)) return; // nil y false se ignoran
        if (Lua.metaOf(v, "__close") == null) {
            throw new LuaError(pos(line) + "variable '" + var + "' got a non-closable value");
        }
        if (scope.toClose == null) scope.toClose = new java.util.ArrayList<>();
        scope.toClose.add(v);
    }

    @Override
    public Object visitLocalFunctionStat(LuaGrammar.LocalFunctionStatContext ctx) {
        String name = text(ctx.NAME());
        Ref r = scope.declare(name, null);
        r.v = makeClosure(name, ctx.funcbody(), false);
        return null;
    }

    @Override
    public Object visitFunctionStat(LuaGrammar.FunctionStatContext ctx) {
        LuaGrammar.FuncnameContext fn = ctx.funcname();
        List<TerminalNode> names = fn.NAME();
        boolean method = fn.COL() != null;
        String fullName = fn.getText();
        Closure c = makeClosure(fullName, ctx.funcbody(), method);
        if (names.size() == 1) {
            setVar(text(names.get(0)), c);
            return null;
        }
        Object obj = getVar(text(names.get(0)));
        int line = line(fn);
        try {
            for (int i = 1; i < names.size() - 1; i++) obj = Lua.index(obj, text(names.get(i)));
            Lua.setIndex(obj, text(names.get(names.size() - 1)), c);
        } catch (LuaError e) {
            throw resolve(e, line, null, null);
        }
        return null;
    }

    Closure makeClosure(String name, LuaGrammar.FuncbodyContext fb, boolean method) {
        LuaGrammar.ParlistContext pl = fb.parlist();
        String[] params;
        boolean vararg = false;
        int extra = method ? 1 : 0;
        if (pl == null) {
            params = new String[extra];
        } else {
            List<TerminalNode> ns = pl.namelist() == null ? java.util.Collections.<TerminalNode>emptyList()
                    : pl.namelist().NAME();
            params = new String[ns.size() + extra];
            for (int i = 0; i < ns.size(); i++) params[i + extra] = text(ns.get(i));
            vararg = pl.DDD() != null;
        }
        if (method) params[0] = "self";
        return new Closure(name, fb.block(), scope, params, vararg);
    }

    @Override
    public Object visitWhileStat(LuaGrammar.WhileStatContext ctx) {
        while (Lua.isTruthy(visit(ctx.exp()))) {
            Object sig = execBlockNewScope(ctx.block());
            if (sig != null) {
                if (sig == BREAK) break;
                return sig;
            }
        }
        return null;
    }

    @Override
    public Object visitRepeatStat(LuaGrammar.RepeatStatContext ctx) {
        while (true) {
            Scope saved = scope;
            scope = new Scope(saved, false);
            try {
                Object sig = execBlock(ctx.block());
                if (sig != null) {
                    if (sig == BREAK) break;
                    return sig;
                }
                // la condicion del 'until' ve las variables locales del cuerpo
                if (Lua.isTruthy(visit(ctx.exp()))) break;
            } finally {
                scope = saved;
            }
        }
        return null;
    }

    @Override
    public Object visitIfStat(LuaGrammar.IfStatContext ctx) {
        // IF exp THEN block (ELSEIF exp THEN block)* (ELSE block)? END
        List<ParseTree> k = ctx.children;
        int i = 0;
        while (true) {
            int t = ((TerminalNode) k.get(i)).getSymbol().getType();
            if (t == LuaLexer.END) return null;
            if (t == LuaLexer.ELSE) return execBlockNewScope((LuaGrammar.BlockContext) k.get(i + 1));
            if (Lua.isTruthy(k.get(i + 1).accept(this))) {
                return execBlockNewScope((LuaGrammar.BlockContext) k.get(i + 3));
            }
            i += 4;
        }
    }

    @Override
    public Object visitNumericForStat(LuaGrammar.NumericForStatContext ctx) {
        String var = text(ctx.NAME());
        int line = line(ctx);
        Object init = visit(ctx.exp(0));
        Object limit = visit(ctx.exp(1));
        Object step = ctx.exp().size() > 2 ? visit(ctx.exp(2)) : (Object) 1L;
        boolean intLoop = init instanceof Long && step instanceof Long;
        init = forNumber(init, "initial value", line);
        limit = forNumber(limit, "limit", line);
        step = forNumber(step, "step", line);
        LuaGrammar.BlockContext body = ctx.block();
        if (intLoop) {
            long i0 = (Long) init, st = (Long) step;
            if (st == 0) throw new LuaError(pos(line) + "'for' step is zero");
            long lim;
            if (limit instanceof Long) {
                lim = (Long) limit;
            } else {
                double fl = (Double) limit;
                if (Double.isNaN(fl)) return null;
                if (st > 0) {
                    fl = Math.floor(fl);
                    if (fl >= 9.223372036854775807E18) lim = Long.MAX_VALUE;
                    else if (fl < -9.223372036854775808E18) return null;
                    else lim = (long) fl;
                } else {
                    fl = Math.ceil(fl);
                    if (fl < -9.223372036854775808E18) lim = Long.MIN_VALUE;
                    else if (fl >= 9.223372036854775807E18) return null;
                    else lim = (long) fl;
                }
            }
            if (st > 0 ? i0 > lim : i0 < lim) return null;
            long count = st > 0 ? (lim - i0) : (i0 - lim);
            if (st != 1) count = Long.divideUnsigned(count, st > 0 ? st : (-(st + 1)) + 1L);
            long i = i0;
            while (true) {
                Object sig = runForBody(var, i, body);
                if (sig != null) {
                    if (sig == BREAK) break;
                    return sig;
                }
                if (count-- == 0) break;
                i += st;
            }
            return null;
        }
        double i = Lua.toDouble(init), lim = Lua.toDouble(limit), st = Lua.toDouble(step);
        if (st == 0) throw new LuaError(pos(line) + "'for' step is zero");
        while (st > 0 ? i <= lim : i >= lim) {
            Object sig = runForBody(var, i, body);
            if (sig != null) {
                if (sig == BREAK) break;
                return sig;
            }
            i += st;
        }
        return null;
    }

    private static Object forNumber(Object v, String what, int line) {
        Object n = Lua.toNumber(v);
        if (n == null) {
            throw new LuaError(pos(line) + "bad 'for' " + what + " (number expected, got " + Lua.typeName(v) + ")");
        }
        return n;
    }

    private Object runForBody(String var, Object value, LuaGrammar.BlockContext body) {
        Scope saved = scope;
        scope = new Scope(saved, false);
        scope.declare(var, value);
        try {
            return execBlock(body);
        } finally {
            scope = saved;
        }
    }

    @Override
    public Object visitGenericForStat(LuaGrammar.GenericForStatContext ctx) {
        List<TerminalNode> nameNodes = ctx.namelist().NAME();
        String[] names = new String[nameNodes.size()];
        for (int i = 0; i < names.length; i++) names[i] = text(nameNodes.get(i));
        Object[] init = evalExpList(ctx.explist());
        Object f = init.length > 0 ? init[0] : null;
        Object s = init.length > 1 ? init[1] : null;
        Object ctl = init.length > 2 ? init[2] : null;
        int line = line(ctx.explist());
        LuaGrammar.BlockContext body = ctx.block();
        boolean fastNext = f == nextFunction && s instanceof LuaTable;
        while (true) {
            Object[] rs;
            if (fastNext) {
                rs = ((LuaTable) s).next(ctl);
                if (rs == null) break;
            } else {
                rs = callValue(f, new Object[] {s, ctl}, line, " (for iterator 'for iterator')");
                if (rs.length == 0 || rs[0] == null) break;
            }
            ctl = rs[0];
            Scope saved = scope;
            scope = new Scope(saved, false);
            try {
                for (int i = 0; i < names.length; i++) scope.declare(names[i], i < rs.length ? rs[i] : null);
                Object sig = execBlock(body);
                if (sig != null) {
                    if (sig == BREAK) break;
                    return sig;
                }
            } finally {
                scope = saved;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------ expresiones simples

    @Override
    public Object visitNilExp(LuaGrammar.NilExpContext ctx) {
        return null;
    }

    @Override
    public Object visitTrueExp(LuaGrammar.TrueExpContext ctx) {
        return Boolean.TRUE;
    }

    @Override
    public Object visitFalseExp(LuaGrammar.FalseExpContext ctx) {
        return Boolean.FALSE;
    }

    @Override
    public Object visitNumberExp(LuaGrammar.NumberExpContext ctx) {
        Object v = ctx.cache;
        if (v == null) {
            v = Lua.str2number(ctx.NUMBER().getText());
            if (v == null) v = 0L;
            ctx.cache = v;
        }
        return v;
    }

    @Override
    public Object visitStringExp(LuaGrammar.StringExpContext ctx) {
        return stringConst(ctx);
    }

    String stringConst(LuaGrammar.StringExpContext ctx) {
        Object v = ctx.cache;
        if (v == null) {
            v = decodeString(ctx.STRING().getText());
            ctx.cache = v;
        }
        return (String) v;
    }

    String stringConst(TerminalNode t) {
        return decodeString(t.getText());
    }

    @Override
    public Object visitVarargExp(LuaGrammar.VarargExpContext ctx) {
        Object[] va = varargs();
        return va.length > 0 ? va[0] : null;
    }

    @Override
    public Object visitFunctionDefExp(LuaGrammar.FunctionDefExpContext ctx) {
        return makeClosure(null, ctx.functiondef().funcbody(), false);
    }

    @Override
    public Object visitPrefixExpr(LuaGrammar.PrefixExprContext ctx) {
        return ctx.children.get(0).accept(this);
    }

    @Override
    public Object visitTableExp(LuaGrammar.TableExpContext ctx) {
        return buildTable(ctx.tableconstructor());
    }

    LuaTable buildTable(LuaGrammar.TableconstructorContext tc) {
        LuaTable t = new LuaTable();
        LuaGrammar.FieldlistContext fl = tc.fieldlist();
        if (fl == null) return t;
        List<LuaGrammar.FieldContext> fs = fl.field();
        int idx = 1;
        int n = fs.size();
        for (int k = 0; k < n; k++) {
            LuaGrammar.FieldContext f = fs.get(k);
            if (f instanceof LuaGrammar.PosFieldContext) {
                LuaGrammar.ExpContext e = ((LuaGrammar.PosFieldContext) f).exp();
                if (k == n - 1 && isMulti(e)) {
                    for (Object v : evalMulti(e)) t.setArrayRaw(idx++, v);
                } else {
                    t.setArrayRaw(idx++, visit(e));
                }
            } else if (f instanceof LuaGrammar.NameFieldContext) {
                LuaGrammar.NameFieldContext nf = (LuaGrammar.NameFieldContext) f;
                t.put(text(nf.NAME()), visit(nf.exp()));
            } else {
                LuaGrammar.KeyFieldContext kf = (LuaGrammar.KeyFieldContext) f;
                Object key = visit(kf.exp(0));
                Object val = visit(kf.exp(1));
                try {
                    t.put(key, val);
                } catch (LuaError e) {
                    throw resolve(e, line(kf), null, null);
                }
            }
        }
        t.trimArray();
        return t;
    }

    // ------------------------------------------------------------------ prefijos

    @Override
    public Object visitParenPrefix(LuaGrammar.ParenPrefixContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public Object visitNamePrefix(LuaGrammar.NamePrefixContext ctx) {
        return getVar(((TerminalNode) ctx.children.get(0)).getSymbol().getText());
    }

    @Override
    public Object visitIndexPrefix(LuaGrammar.IndexPrefixContext ctx) {
        Object obj = visit(ctx.prefixexp());
        Object key = visit(ctx.exp());
        if (obj instanceof LuaTable) {
            LuaTable t = (LuaTable) obj;
            Object v = t.get(key);
            if (v != null || t.metatable == null) return v;
        }
        try {
            return Lua.index(obj, key);
        } catch (LuaError e) {
            throw resolve(e, line(ctx.exp()), ctx.prefixexp(), null);
        }
    }

    @Override
    public Object visitFieldPrefix(LuaGrammar.FieldPrefixContext ctx) {
        Object obj = visit(ctx.prefixexp());
        String key = text(ctx.NAME());
        if (obj instanceof LuaTable) {
            LuaTable t = (LuaTable) obj;
            Object v = t.get(key);
            if (v != null || t.metatable == null) return v;
        }
        try {
            return Lua.index(obj, key);
        } catch (LuaError e) {
            throw resolve(e, line(ctx.NAME()), ctx.prefixexp(), null);
        }
    }

    @Override
    public Object visitCallPrefix(LuaGrammar.CallPrefixContext ctx) {
        return Lua.first(evalCall((LuaGrammar.PrefixexpContext) ctx.children.get(0),
                (LuaGrammar.ArgsContext) ctx.children.get(1)));
    }

    @Override
    public Object visitMethodPrefix(LuaGrammar.MethodPrefixContext ctx) {
        return Lua.first(evalMethodCall(ctx.prefixexp(), ctx.NAME(), ctx.args()));
    }

    // ------------------------------------------------------------------ operadores

    private Object arith(int op, ParserRuleContext ctx, Object a, Object b,
                         ParserRuleContext left, ParserRuleContext right) {
        try {
            return Lua.arith(op, a, b);
        } catch (LuaError e) {
            throw resolve(e, op == Lua.UNM || op == Lua.BNOT ? line(ctx) : opLine(ctx), left, right);
        }
    }

    @Override
    public Object visitAddExp(LuaGrammar.AddExpContext ctx) {
        Object a = left(ctx).accept(this);
        Object b = right(ctx).accept(this);
        boolean plus = opType(ctx) == LuaLexer.PLUS;
        if (a instanceof Long && b instanceof Long) return plus ? (Long) a + (Long) b : (Long) a - (Long) b;
        if (a instanceof Double && b instanceof Double) return plus ? (Double) a + (Double) b : (Double) a - (Double) b;
        return arith(plus ? Lua.ADD : Lua.SUB, ctx, a, b, left(ctx), right(ctx));
    }

    @Override
    public Object visitMulExp(LuaGrammar.MulExpContext ctx) {
        Object a = left(ctx).accept(this);
        Object b = right(ctx).accept(this);
        int t = opType(ctx);
        int op = t == LuaLexer.STAR ? Lua.MUL : t == LuaLexer.SLASH ? Lua.DIV : t == LuaLexer.SS ? Lua.IDIV : Lua.MOD;
        if (op == Lua.MUL) {
            if (a instanceof Long && b instanceof Long) return (Long) a * (Long) b;
            if (a instanceof Double && b instanceof Double) return (Double) a * (Double) b;
        }
        return arith(op, ctx, a, b, left(ctx), right(ctx));
    }

    @Override
    public Object visitPowExp(LuaGrammar.PowExpContext ctx) {
        Object a = left(ctx).accept(this);
        Object b = right(ctx).accept(this);
        return arith(Lua.POW, ctx, a, b, left(ctx), right(ctx));
    }

    @Override
    public Object visitShiftExp(LuaGrammar.ShiftExpContext ctx) {
        Object a = left(ctx).accept(this);
        Object b = right(ctx).accept(this);
        return arith(opType(ctx) == LuaLexer.LL ? Lua.SHL : Lua.SHR, ctx, a, b, left(ctx), right(ctx));
    }

    @Override
    public Object visitBandExp(LuaGrammar.BandExpContext ctx) {
        return arith(Lua.BAND, ctx, visit(left(ctx)), visit(right(ctx)), left(ctx), right(ctx));
    }

    @Override
    public Object visitBorExp(LuaGrammar.BorExpContext ctx) {
        return arith(Lua.BOR, ctx, visit(left(ctx)), visit(right(ctx)), left(ctx), right(ctx));
    }

    @Override
    public Object visitBxorExp(LuaGrammar.BxorExpContext ctx) {
        return arith(Lua.BXOR, ctx, visit(left(ctx)), visit(right(ctx)), left(ctx), right(ctx));
    }

    @Override
    public Object visitUnaryExp(LuaGrammar.UnaryExpContext ctx) {
        int t = ((TerminalNode) ctx.getChild(0)).getSymbol().getType();
        Object a = visit(ctx.exp());
        switch (t) {
            case LuaLexer.NOT:
                return !Lua.isTruthy(a);
            case LuaLexer.MINUS:
                if (a instanceof Long) return -(Long) a;
                if (a instanceof Double) return -(Double) a;
                return arith(Lua.UNM, ctx, a, a, ctx.exp(), ctx.exp());
            case LuaLexer.POUND:
                if (a instanceof String) return (long) ((String) a).length();
                if (a instanceof LuaTable && ((LuaTable) a).metatable == null) return ((LuaTable) a).length();
                try {
                    return Lua.length(a);
                } catch (LuaError e) {
                    throw resolve(e, line(ctx), ctx.exp(), null);
                }
            default:
                return arith(Lua.BNOT, ctx, a, a, ctx.exp(), ctx.exp());
        }
    }

    @Override
    public Object visitConcatExp(LuaGrammar.ConcatExpContext ctx) {
        Object a = left(ctx).accept(this);
        Object b = right(ctx).accept(this);
        if (a instanceof String && b instanceof String) return ((String) a).concat((String) b);
        try {
            return Lua.concat(a, b);
        } catch (LuaError e) {
            throw resolve(e, opLine(ctx), left(ctx), right(ctx));
        }
    }

    @Override
    public Object visitRelExp(LuaGrammar.RelExpContext ctx) {
        Object a = left(ctx).accept(this);
        Object b = right(ctx).accept(this);
        int t = opType(ctx);
        try {
            switch (t) {
                case LuaLexer.EE: return Lua.equals(a, b);
                case LuaLexer.SQEQ: return !Lua.equals(a, b);
                case LuaLexer.LT: return Lua.lessThan(a, b);
                case LuaLexer.GT: return Lua.lessThan(b, a);
                case LuaLexer.LE: return Lua.lessEqual(a, b);
                default: return Lua.lessEqual(b, a);
            }
        } catch (LuaError e) {
            throw resolve(e, opLine(ctx), null, null);
        }
    }

    @Override
    public Object visitAndExp(LuaGrammar.AndExpContext ctx) {
        Object a = left(ctx).accept(this);
        return Lua.isTruthy(a) ? visit(right(ctx)) : a;
    }

    @Override
    public Object visitOrExp(LuaGrammar.OrExpContext ctx) {
        Object a = left(ctx).accept(this);
        return Lua.isTruthy(a) ? a : visit(right(ctx));
    }

    // ------------------------------------------------------------------ strings literales

    static String decodeString(String raw) {
        char q = raw.charAt(0);
        if (q == '[') {
            int eq = 0;
            int i = 1;
            while (raw.charAt(i) == '=') {
                eq++;
                i++;
            }
            String body = raw.substring(i + 1, raw.length() - 2 - eq);
            if (body.startsWith("\r\n") || body.startsWith("\n\r")) body = body.substring(2);
            else if (body.startsWith("\n") || body.startsWith("\r")) body = body.substring(1);
            return body.replace("\r\n", "\n").replace("\n\r", "\n").replace('\r', '\n');
        }
        StringBuilder sb = new StringBuilder(raw.length());
        int end = raw.length() - 1;
        for (int i = 1; i < end; i++) {
            char c = raw.charAt(i);
            if (c != '\\') {
                sb.append(c);
                continue;
            }
            char e = raw.charAt(++i);
            switch (e) {
                case 'a': sb.append('\u0007'); break;
                case 'b': sb.append('\b'); break;
                case 'f': sb.append('\f'); break;
                case 'n': sb.append('\n'); break;
                case 'r': sb.append('\r'); break;
                case 't': sb.append('\t'); break;
                case 'v': sb.append('\u000B'); break;
                case '\\': sb.append('\\'); break;
                case '"': sb.append('"'); break;
                case '\'': sb.append('\''); break;
                case '\n':
                case '\r':
                    sb.append('\n');
                    if (i + 1 < end && (raw.charAt(i + 1) == '\n' || raw.charAt(i + 1) == '\r')
                            && raw.charAt(i + 1) != e) i++;
                    break;
                case 'x': {
                    int v = Integer.parseInt(raw.substring(i + 1, i + 3), 16);
                    sb.append((char) v);
                    i += 2;
                    break;
                }
                case 'z': {
                    while (i + 1 < end && Character.isWhitespace(raw.charAt(i + 1))) i++;
                    break;
                }
                case 'u': {
                    int close = raw.indexOf('}', i);
                    long cp = Long.parseLong(raw.substring(i + 2, close), 16);
                    appendUtf8(sb, cp);
                    i = close;
                    break;
                }
                default: {
                    if (e >= '0' && e <= '9') {
                        int v = 0, k = 0;
                        while (k < 3 && i < end && raw.charAt(i) >= '0' && raw.charAt(i) <= '9') {
                            v = v * 10 + (raw.charAt(i) - '0');
                            i++;
                            k++;
                        }
                        i--;
                        sb.append((char) (v & 0xFF));
                    } else {
                        sb.append(e);
                    }
                }
            }
        }
        return sb.toString();
    }

    /** Agrega el codigo como bytes UTF-8 (un char por byte). */
    static void appendUtf8(StringBuilder sb, long cp) {
        if (cp < 0x80) {
            sb.append((char) cp);
        } else if (cp < 0x800) {
            sb.append((char) (0xC0 | (cp >> 6)));
            sb.append((char) (0x80 | (cp & 0x3F)));
        } else if (cp < 0x10000) {
            sb.append((char) (0xE0 | (cp >> 12)));
            sb.append((char) (0x80 | ((cp >> 6) & 0x3F)));
            sb.append((char) (0x80 | (cp & 0x3F)));
        } else if (cp < 0x200000) {
            sb.append((char) (0xF0 | (cp >> 18)));
            sb.append((char) (0x80 | ((cp >> 12) & 0x3F)));
            sb.append((char) (0x80 | ((cp >> 6) & 0x3F)));
            sb.append((char) (0x80 | (cp & 0x3F)));
        } else if (cp < 0x4000000) {
            sb.append((char) (0xF8 | (cp >> 24)));
            sb.append((char) (0x80 | ((cp >> 18) & 0x3F)));
            sb.append((char) (0x80 | ((cp >> 12) & 0x3F)));
            sb.append((char) (0x80 | ((cp >> 6) & 0x3F)));
            sb.append((char) (0x80 | (cp & 0x3F)));
        } else {
            sb.append((char) (0xFC | (cp >> 30)));
            sb.append((char) (0x80 | ((cp >> 24) & 0x3F)));
            sb.append((char) (0x80 | ((cp >> 18) & 0x3F)));
            sb.append((char) (0x80 | ((cp >> 12) & 0x3F)));
            sb.append((char) (0x80 | ((cp >> 6) & 0x3F)));
            sb.append((char) (0x80 | (cp & 0x3F)));
        }
    }

    /** Linea de la llamada en el nivel indicado (1 = quien llamo a error). 0 si no hay. */
    int callLine(int level) {
        if (level == 1) return state.line;
        int idx = state.depth - (level - 1);
        if (idx < 0 || idx >= state.depth) return 0;
        return state.lines[idx];
    }
}
