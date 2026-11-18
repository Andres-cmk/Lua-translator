import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

/** Libreria estandar de Lua 5.4 (lo necesario para los programas de prueba y un poco mas). */
final class StdLib {
    private StdLib() {}

    static PrintStream out = System.out;
    static Interpreter I;
    private static final long START = System.nanoTime();

    // ------------------------------------------------------------------ ayudas para argumentos

    static Object arg(Object[] a, int i) {
        return i < a.length ? a[i] : null;
    }

    static String typeOfArg(Object[] a, int i) {
        return i < a.length ? Lua.typeName(a[i]) : "no value";
    }

    static LuaError argError(int i, String fname, String msg) {
        return new LuaError("bad argument #" + (i + 1) + " to '" + fname + "' (" + msg + ")", true);
    }

    static LuaError typeError(Object[] a, int i, String fname, String expected) {
        return argError(i, fname, expected + " expected, got " + typeOfArg(a, i));
    }

    static LuaTable checkTable(Object[] a, int i, String fname) {
        Object v = arg(a, i);
        if (v instanceof LuaTable) return (LuaTable) v;
        throw typeError(a, i, fname, "table");
    }

    static long checkInt(Object[] a, int i, String fname) {
        Object v = arg(a, i);
        if (v instanceof Long) return (Long) v;
        Object n = Lua.toNumber(v);
        if (n == null) throw typeError(a, i, fname, "number");
        Long l = Lua.toInteger(n);
        if (l == null) throw argError(i, fname, "number has no integer representation");
        return l;
    }

    static long optInt(Object[] a, int i, String fname, long def) {
        return arg(a, i) == null ? def : checkInt(a, i, fname);
    }

    static Object checkNumber(Object[] a, int i, String fname) {
        Object n = Lua.toNumber(arg(a, i));
        if (n == null) throw typeError(a, i, fname, "number");
        return n;
    }

    static double checkDouble(Object[] a, int i, String fname) {
        return Lua.toDouble(checkNumber(a, i, fname));
    }

    static String checkString(Object[] a, int i, String fname) {
        Object v = arg(a, i);
        if (v instanceof String) return (String) v;
        if (v instanceof Long || v instanceof Double) return Lua.tostring(v);
        throw typeError(a, i, fname, "string");
    }

    static String optString(Object[] a, int i, String fname, String def) {
        return arg(a, i) == null ? def : checkString(a, i, fname);
    }

    static void checkAny(Object[] a, int i, String fname) {
        if (i >= a.length) throw argError(i, fname, "value expected");
    }

    static Object[] ret(Object v) {
        return new Object[] {v};
    }

    static final Object[] NIL = new Object[] {null};

    static void reg(LuaTable t, String name, LuaFunction.Body body) {
        t.put(name, LuaFunction.of(name, body));
    }

    /** Asigna "string.rep", "table.insert", etc. a las funciones de cada libreria. */
    private static void qualify(LuaTable lib, String prefix) {
        Object[] kv = lib.next(null);
        while (kv != null) {
            if (kv[1] instanceof LuaFunction) ((LuaFunction) kv[1]).qualifiedName = prefix + "." + kv[0];
            kv = lib.next(kv[0]);
        }
    }

    // ------------------------------------------------------------------ instalacion

    static void install(Interpreter interp) {
        I = interp;
        LuaTable G = interp.globals;
        G.put("_G", G);
        G.put("_VERSION", "Lua 5.4");
        installBase(G);
        LuaTable string = installString();
        G.put("string", string);
        LuaTable smeta = new LuaTable();
        smeta.put("__index", string);
        Lua.stringMeta = smeta;
        G.put("table", installTable());
        G.put("math", installMath());
        G.put("os", installOs());
        G.put("io", installIo());
        G.put("coroutine", installCoroutine());
        G.put("utf8", installUtf8());
        for (String lib : new String[] {"string", "table", "math", "os", "io", "coroutine", "utf8"}) {
            qualify((LuaTable) G.get(lib), lib);
        }
    }

    // ------------------------------------------------------------------ base

    private static void installBase(LuaTable G) {
        reg(G, "print", a -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
                if (i > 0) sb.append('\t');
                sb.append(Lua.tostring(a[i]));
            }
            sb.append('\n');
            out.print(sb);
            return Lua.NONE;
        });
        reg(G, "type", a -> {
            checkAny(a, 0, "type");
            return ret(Lua.typeName(a[0]));
        });
        reg(G, "tostring", a -> {
            checkAny(a, 0, "tostring");
            return ret(Lua.tostring(a[0]));
        });
        reg(G, "tonumber", a -> {
            if (arg(a, 1) == null) {
                checkAny(a, 0, "tonumber");
                Object v = a[0];
                if (v instanceof Long || v instanceof Double) return ret(v);
                if (v instanceof String) return ret(Lua.str2number((String) v));
                return NIL;
            }
            long base = checkInt(a, 1, "tonumber");
            Object v = arg(a, 0);
            if (!(v instanceof String)) throw typeError(a, 0, "tonumber", "string");
            if (base < 2 || base > 36) throw argError(1, "tonumber", "base out of range");
            String s = ((String) v).trim();
            boolean neg = false;
            if (s.startsWith("-")) {
                neg = true;
                s = s.substring(1);
            }
            if (s.isEmpty()) return NIL;
            long n = 0;
            for (int i = 0; i < s.length(); i++) {
                int d = Character.digit(s.charAt(i), 36);
                if (d < 0 || d >= base || s.charAt(i) > 127) return NIL;
                n = n * base + d;
            }
            return ret(neg ? -n : n);
        });
        LuaFunction ipairsIter = LuaFunction.of("ipairs_iter", a -> {
            long i = (Long) a[1] + 1;
            Object t = a[0];
            Object v = (t instanceof LuaTable && ((LuaTable) t).metatable == null)
                    ? ((LuaTable) t).get(i) : Lua.index(t, i);
            return v == null ? NIL : new Object[] {i, v};
        });
        reg(G, "ipairs", a -> {
            checkAny(a, 0, "ipairs");
            return new Object[] {ipairsIter, a[0], 0L};
        });
        LuaFunction next = LuaFunction.of("next", a -> {
            LuaTable t = checkTable(a, 0, "next");
            Object[] r = t.next(arg(a, 1));
            return r == null ? NIL : r;
        });
        I.nextFunction = next;
        G.put("next", next);
        reg(G, "pairs", a -> {
            Object t = arg(a, 0);
            Object h = Lua.metaOf(t, "__pairs");
            if (h != null) {
                Object[] r = Lua.call(h, t);
                return new Object[] {arg(r, 0), arg(r, 1), arg(r, 2)};
            }
            checkTable(a, 0, "pairs");
            return new Object[] {next, t, null};
        });
        reg(G, "select", a -> {
            Object n = arg(a, 0);
            if ("#".equals(n)) return ret((long) (a.length - 1));
            long i = checkInt(a, 0, "select");
            int count = a.length - 1;
            if (i < 0) i = count + i;
            else if (i == 0) throw argError(0, "select", "index out of range");
            else i = i - 1;
            if (i < 0) throw argError(0, "select", "index out of range");
            if (i >= count) return Lua.NONE;
            return Arrays.copyOfRange(a, (int) i + 1, a.length);
        });
        reg(G, "rawget", a -> ret(checkTable(a, 0, "rawget").get(arg(a, 1))));
        reg(G, "rawset", a -> {
            checkTable(a, 0, "rawset").put(arg(a, 1), arg(a, 2));
            return ret(a[0]);
        });
        reg(G, "rawequal", a -> ret(Lua.rawEquals(arg(a, 0), arg(a, 1))));
        reg(G, "rawlen", a -> {
            Object v = arg(a, 0);
            if (v instanceof LuaTable) return ret(((LuaTable) v).length());
            if (v instanceof String) return ret((long) ((String) v).length());
            throw argError(0, "rawlen", "table or string expected");
        });
        reg(G, "setmetatable", a -> {
            LuaTable t = checkTable(a, 0, "setmetatable");
            Object mt = arg(a, 1);
            if (mt != null && !(mt instanceof LuaTable)) throw typeError(a, 1, "setmetatable", "nil or table");
            if (t.metatable != null && t.metatable.get("__metatable") != null) {
                throw new LuaError("cannot change a protected metatable", true);
            }
            t.metatable = (LuaTable) mt;
            if (mt != null && ((LuaTable) mt).get("__mode") instanceof String) GarbageCollector.register(t);
            return ret(t);
        });
        reg(G, "getmetatable", a -> {
            LuaTable mt = Lua.getMetatable(arg(a, 0));
            if (mt == null) return NIL;
            Object p = mt.get("__metatable");
            return ret(p != null ? p : mt);
        });
        reg(G, "assert", a -> {
            if (a.length > 0 && Lua.isTruthy(a[0])) return a;
            if (a.length == 0) throw argError(0, "assert", "value expected");
            Object msg = a.length > 1 ? a[1] : "assertion failed!";
            if (msg instanceof String && I.state.line > 0) msg = Interpreter.pos(I.state.line) + msg;
            throw new LuaError(msg);
        });
        reg(G, "error", a -> {
            Object v = arg(a, 0);
            long level = optInt(a, 1, "error", 1);
            if (v instanceof String && level > 0) {
                int line = I.callLine((int) level);
                if (line > 0) v = Interpreter.pos(line) + v;
            }
            throw new LuaError(v);
        });
        reg(G, "pcall", a -> {
            checkAny(a, 0, "pcall");
            Object f = a[0];
            Object[] rest = Arrays.copyOfRange(a, 1, a.length);
            try {
                Object[] r = protectedCall(f, rest);
                Object[] res = new Object[r.length + 1];
                res[0] = Boolean.TRUE;
                System.arraycopy(r, 0, res, 1, r.length);
                return res;
            } catch (LuaError e) {
                return new Object[] {Boolean.FALSE, e.value};
            }
        });
        reg(G, "xpcall", a -> {
            Object f = arg(a, 0);
            Object h = arg(a, 1);
            Object[] rest = a.length > 2 ? Arrays.copyOfRange(a, 2, a.length) : Lua.NONE;
            try {
                Object[] r = protectedCall(f, rest);
                Object[] res = new Object[r.length + 1];
                res[0] = Boolean.TRUE;
                System.arraycopy(r, 0, res, 1, r.length);
                return res;
            } catch (LuaError e) {
                Object[] hr = protectedCall(h, new Object[] {e.value});
                Object[] res = new Object[hr.length + 1];
                res[0] = Boolean.FALSE;
                System.arraycopy(hr, 0, res, 1, hr.length);
                return res;
            }
        });
        reg(G, "load", a -> {
            Object chunk = arg(a, 0);
            String src;
            if (chunk instanceof String) {
                src = (String) chunk;
            } else if (chunk instanceof LuaFunction) {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    Object piece = Lua.first(Lua.call(chunk));
                    if (piece == null || "".equals(piece)) break;
                    sb.append(Lua.tostring(piece));
                }
                src = sb.toString();
            } else {
                throw typeError(a, 0, "load", "string");
            }
            String name = optString(a, 1, "load", src);
            try {
                LuaGrammar.ChunkContext c = Main.parse(src);
                return ret(I.makeChunk(c, name));
            } catch (Main.SyntaxError e) {
                return new Object[] {null, "[string \"" + shortSrc(name) + "\"]:" + e.getMessage()};
            }
        });
        reg(G, "collectgarbage", a -> {
            String opt = optString(a, 0, "collectgarbage", "collect");
            if ("count".equals(opt)) {
                Runtime rt = Runtime.getRuntime();
                double kb = (rt.totalMemory() - rt.freeMemory()) / 1024.0;
                return new Object[] {kb, 0L};
            }
            if ("isrunning".equals(opt)) return ret(Boolean.TRUE);
            if ("collect".equals(opt) || "step".equals(opt)) GarbageCollector.collect(I);
            return ret(0L);
        });
    }

    private static String shortSrc(String s) {
        int nl = s.indexOf('\n');
        if (nl >= 0) return s.substring(0, nl) + "...";
        if (s.length() > 40) return s.substring(0, 37) + "...";
        return s;
    }

    /** Llamada protegida (pcall): las fallas de Java se convierten en errores de Lua. */
    static Object[] protectedCall(Object f, Object[] args) {
        if (!(f instanceof LuaFunction) && Lua.metaOf(f, "__call") == null) {
            throw new LuaError("attempt to call a " + Lua.typeName(f) + " value");
        }
        Interpreter.Scope savedScope = I.scope;
        int savedDepth = I.state.depth;
        try {
            return I.callValue(f, args, 0, null);
        } catch (LuaError e) {
            e.needsPosition = false;
            if (f instanceof LuaFunction && ((LuaFunction) f).qualifiedName != null && e.value instanceof String) {
                LuaFunction lf = (LuaFunction) f;
                e.value = ((String) e.value).replace("to '" + lf.name + "'", "to '" + lf.qualifiedName + "'");
            }
            throw e;
        } catch (StackOverflowError e) {
            throw new LuaError("stack overflow");
        } catch (RuntimeException e) {
            throw new LuaError(String.valueOf(e));
        } finally {
            I.scope = savedScope;
            I.state.depth = savedDepth;
        }
    }

    // ------------------------------------------------------------------ string

    static long posrelat(long pos, int len) {
        if (pos > 0) return pos;
        if (pos == 0) return 1;
        if (pos < -(long) len) return 1;
        return len + pos + 1;
    }

    static long endpos(long pos, int len) {
        if (pos > len) return len;
        if (pos >= 0) return pos;
        if (pos < -(long) len) return 0;
        return len + pos + 1;
    }

    private static LuaTable installString() {
        LuaTable S = new LuaTable();
        reg(S, "len", a -> ret((long) checkString(a, 0, "len").length()));
        reg(S, "sub", a -> {
            String s = checkString(a, 0, "sub");
            long i = posrelat(checkInt(a, 1, "sub"), s.length());
            long j = endpos(optInt(a, 2, "sub", -1), s.length());
            if (i > j) return ret("");
            return ret(s.substring((int) i - 1, (int) j));
        });
        reg(S, "upper", a -> {
            char[] cs = checkString(a, 0, "upper").toCharArray();
            for (int i = 0; i < cs.length; i++) if (cs[i] >= 'a' && cs[i] <= 'z') cs[i] -= 32;
            return ret(new String(cs));
        });
        reg(S, "lower", a -> {
            char[] cs = checkString(a, 0, "lower").toCharArray();
            for (int i = 0; i < cs.length; i++) if (cs[i] >= 'A' && cs[i] <= 'Z') cs[i] += 32;
            return ret(new String(cs));
        });
        reg(S, "rep", a -> {
            String s = checkString(a, 0, "rep");
            long n = checkInt(a, 1, "rep");
            String sep = optString(a, 2, "rep", "");
            if (n <= 0) return ret("");
            long total = (s.length() + sep.length()) * n;
            if (total > 200_000_000L) throw new LuaError("resulting string too large", true);
            StringBuilder sb = new StringBuilder((int) total);
            for (long i = 0; i < n; i++) {
                if (i > 0) sb.append(sep);
                sb.append(s);
            }
            return ret(sb.toString());
        });
        reg(S, "reverse", a -> ret(new StringBuilder(checkString(a, 0, "reverse")).reverse().toString()));
        reg(S, "byte", a -> {
            String s = checkString(a, 0, "byte");
            long i = posrelat(optInt(a, 1, "byte", 1), s.length());
            long j = endpos(optInt(a, 2, "byte", i), s.length());
            if (i > j) return Lua.NONE;
            Object[] r = new Object[(int) (j - i + 1)];
            for (int k = 0; k < r.length; k++) r[k] = (long) (s.charAt((int) i - 1 + k) & 0xFF);
            return r;
        });
        reg(S, "char", a -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
                long c = checkInt(a, i, "char");
                if (c < 0 || c > 255) throw argError(i, "char", "value out of range");
                sb.append((char) c);
            }
            return ret(sb.toString());
        });
        reg(S, "format", StdLib::format);
        reg(S, "find", a -> find(a, true));
        reg(S, "match", a -> find(a, false));
        reg(S, "gmatch", a -> {
            String s = checkString(a, 0, "gmatch");
            String p = checkString(a, 1, "gmatch");
            long init = posrelat(optInt(a, 2, "gmatch", 1), s.length());
            final int[] st = {(int) Math.min(init - 1, s.length() + 1), -1}; // posicion, ultimo match
            return ret(LuaFunction.of("gmatch_iter", x -> {
                LuaPattern ms = new LuaPattern(s, p);
                for (int src = st[0]; src <= s.length(); src++) {
                    ms.reprep();
                    int e = ms.match(src, 0);
                    if (e != -1 && e != st[1]) {
                        st[0] = st[1] = e;
                        return ms.getCaptures(src, e, true);
                    }
                }
                st[0] = s.length() + 1;
                return NIL;
            }));
        });
        reg(S, "gsub", StdLib::gsub);
        return S;
    }

    private static Object[] find(Object[] a, boolean isFind) {
        String fname = isFind ? "find" : "match";
        String s = checkString(a, 0, fname);
        String p = checkString(a, 1, fname);
        long init = posrelat(optInt(a, 2, fname, 1), s.length());
        if (init > s.length() + 1) return NIL;
        if (isFind && (Lua.isTruthy(arg(a, 3)) || LuaPattern.noSpecials(p))) {
            int idx = s.indexOf(p, (int) init - 1);
            if (idx < 0) return NIL;
            return new Object[] {(long) idx + 1, (long) (idx + p.length())};
        }
        LuaPattern ms = new LuaPattern(s, p);
        boolean anchor = p.length() > 0 && p.charAt(0) == '^';
        int pi = anchor ? 1 : 0;
        int s1 = (int) init - 1;
        do {
            ms.reprep();
            int e = ms.match(s1, pi);
            if (e != -1) {
                if (isFind) {
                    Object[] caps = ms.getCaptures(s1, e, false);
                    Object[] r = new Object[2 + caps.length];
                    r[0] = (long) s1 + 1;
                    r[1] = (long) e;
                    System.arraycopy(caps, 0, r, 2, caps.length);
                    return r;
                }
                return ms.getCaptures(s1, e, true);
            }
        } while (s1++ < s.length() && !anchor);
        return NIL;
    }

    private static Object[] gsub(Object[] a) {
        String src = checkString(a, 0, "gsub");
        String p = checkString(a, 1, "gsub");
        Object repl = arg(a, 2);
        if (!(repl instanceof String || repl instanceof Long || repl instanceof Double
                || repl instanceof LuaTable || repl instanceof LuaFunction)) {
            throw typeError(a, 2, "gsub", "string/function/table");
        }
        long maxN = optInt(a, 3, "gsub", (long) src.length() + 1);
        boolean anchor = p.length() > 0 && p.charAt(0) == '^';
        int pi = anchor ? 1 : 0;
        LuaPattern ms = new LuaPattern(src, p);
        StringBuilder b = new StringBuilder();
        int s = 0, lastMatch = -1;
        long n = 0;
        while (n < maxN) {
            ms.reprep();
            int e = ms.match(s, pi);
            if (e != -1 && e != lastMatch) {
                n++;
                addValue(ms, b, s, e, repl);
                s = lastMatch = e;
            } else if (s < src.length()) {
                b.append(src.charAt(s++));
            } else {
                break;
            }
            if (anchor) break;
        }
        if (s < src.length()) b.append(src, s, src.length());
        return new Object[] {b.toString(), n};
    }

    private static void addValue(LuaPattern ms, StringBuilder b, int s, int e, Object repl) {
        Object v;
        if (repl instanceof String || repl instanceof Long || repl instanceof Double) {
            String r = Lua.tostring(repl);
            for (int i = 0; i < r.length(); i++) {
                char c = r.charAt(i);
                if (c != '%') {
                    b.append(c);
                    continue;
                }
                i++;
                if (i >= r.length()) throw new LuaError("invalid use of '%' in replacement string", true);
                char d = r.charAt(i);
                if (d == '%') {
                    b.append('%');
                } else if (d >= '0' && d <= '9') {
                    Object cap = d == '0' ? ms.src.substring(s, e) : ms.getCapture(d - '1', s, e);
                    b.append(Lua.tostring(cap));
                } else {
                    throw new LuaError("invalid use of '%' in replacement string", true);
                }
            }
            return;
        }
        if (repl instanceof LuaTable) {
            v = Lua.index(repl, ms.getCapture(0, s, e));
        } else {
            v = Lua.first(Lua.call(repl, ms.getCaptures(s, e, true)));
        }
        if (!Lua.isTruthy(v)) {
            b.append(ms.src, s, e);
        } else if (v instanceof String || v instanceof Long || v instanceof Double) {
            b.append(Lua.tostring(v));
        } else {
            throw new LuaError("invalid replacement value (a " + Lua.typeName(v) + ")", true);
        }
    }

    // ------------------------------------------------------------------ string.format

    private static Object[] format(Object[] a) {
        String fmt = checkString(a, 0, "format");
        StringBuilder sb = new StringBuilder();
        int argi = 1;
        int i = 0;
        while (i < fmt.length()) {
            char c = fmt.charAt(i++);
            if (c != '%') {
                sb.append(c);
                continue;
            }
            if (i >= fmt.length()) throw new LuaError("invalid conversion '%' to 'format'", true);
            if (fmt.charAt(i) == '%') {
                sb.append('%');
                i++;
                continue;
            }
            int start = i;
            StringBuilder flags = new StringBuilder();
            while (i < fmt.length() && "-+ #0".indexOf(fmt.charAt(i)) >= 0) flags.append(fmt.charAt(i++));
            int width = -1, prec = -1;
            int ws = i;
            while (i < fmt.length() && Character.isDigit(fmt.charAt(i))) i++;
            if (i > ws) width = Integer.parseInt(fmt.substring(ws, i));
            if (i < fmt.length() && fmt.charAt(i) == '.') {
                i++;
                int ps = i;
                while (i < fmt.length() && Character.isDigit(fmt.charAt(i))) i++;
                prec = i > ps ? Integer.parseInt(fmt.substring(ps, i)) : 0;
            }
            if (i >= fmt.length()) {
                throw new LuaError("invalid conversion '%" + fmt.substring(start) + "' to 'format'", true);
            }
            char conv = fmt.charAt(i++);
            String spec = fmt.substring(start, i);
            if (width > 99 || prec > 99) throw new LuaError("invalid conversion '%" + spec + "' to 'format'", true);
            String fl = flags.toString();
            if (argi >= a.length && conv != '%') throw argError(argi, "format", "no value");
            switch (conv) {
                case 'c':
                    sb.append(pad(String.valueOf((char) checkInt(a, argi++, "format")), width, fl.contains("-"), false));
                    break;
                case 'd':
                case 'i': {
                    long n = checkInt(a, argi++, "format");
                    sb.append(formatInt(n, fl, width, prec));
                    break;
                }
                case 'u': {
                    long n = checkInt(a, argi++, "format");
                    sb.append(formatInt(n, fl, width, prec));
                    break;
                }
                case 'o':
                case 'x':
                case 'X': {
                    long n = checkInt(a, argi++, "format");
                    String digits = conv == 'o' ? Long.toOctalString(n) : Long.toHexString(n);
                    if (conv == 'X') digits = digits.toUpperCase(Locale.ROOT);
                    if (prec >= 0) {
                        while (digits.length() < prec) digits = "0" + digits;
                        if (prec == 0 && n == 0) digits = "";
                    }
                    String prefix = "";
                    if (fl.contains("#") && n != 0) prefix = conv == 'o' ? "0" : conv == 'x' ? "0x" : "0X";
                    sb.append(padNumber(prefix, digits, width, fl, prec < 0));
                    break;
                }
                case 'e':
                case 'E':
                case 'f':
                case 'F':
                case 'g':
                case 'G': {
                    double d = checkDouble(a, argi++, "format");
                    sb.append(formatFloat(d, conv, fl, width, prec));
                    break;
                }
                case 'a':
                case 'A': {
                    double d = checkDouble(a, argi++, "format");
                    String h = hexFloat(d);
                    if (conv == 'A') h = h.toUpperCase(Locale.ROOT);
                    sb.append(pad(h, width, fl.contains("-"), false));
                    break;
                }
                case 's': {
                    String s = Lua.tostring(a[argi++]);
                    if (prec >= 0 && s.length() > prec) s = s.substring(0, prec);
                    sb.append(pad(s, width, fl.contains("-"), false));
                    break;
                }
                case 'q':
                    sb.append(quote(a[argi++]));
                    break;
                default:
                    throw new LuaError("invalid conversion '%" + spec + "' to 'format'", true);
            }
        }
        return ret(sb.toString());
    }

    private static String pad(String s, int width, boolean left, boolean zero) {
        if (width <= s.length()) return s;
        StringBuilder sb = new StringBuilder();
        if (left) {
            sb.append(s);
            while (sb.length() < width) sb.append(' ');
            return sb.toString();
        }
        for (int i = s.length(); i < width; i++) sb.append(zero ? '0' : ' ');
        return sb.append(s).toString();
    }

    /** Relleno de numeros: signo/prefijo, ceros (flag 0) o espacios, alineacion (flag -). */
    private static String padNumber(String sign, String digits, int width, String fl, boolean zeroAllowed) {
        int len = sign.length() + digits.length();
        if (width <= len) return sign + digits;
        if (fl.contains("-")) return pad(sign + digits, width, true, false);
        if (fl.contains("0") && zeroAllowed) {
            StringBuilder sb = new StringBuilder(sign);
            for (int i = len; i < width; i++) sb.append('0');
            return sb.append(digits).toString();
        }
        return pad(sign + digits, width, false, false);
    }

    private static String formatInt(long n, String fl, int width, int prec) {
        String digits = n < 0 ? (n == Long.MIN_VALUE ? "9223372036854775808" : Long.toString(-n)) : Long.toString(n);
        if (prec >= 0) {
            while (digits.length() < prec) digits = "0" + digits;
            if (prec == 0 && n == 0) digits = "";
        }
        String sign = n < 0 ? "-" : fl.contains("+") ? "+" : fl.contains(" ") ? " " : "";
        return padNumber(sign, digits, width, fl, prec < 0);
    }

    private static String formatFloat(double d, char conv, String fl, int width, int prec) {
        boolean upper = Character.isUpperCase(conv);
        String sign = (d < 0 || (d == 0 && 1 / d < 0)) ? "-" : fl.contains("+") ? "+" : fl.contains(" ") ? " " : "";
        double ad = Math.abs(d);
        String body;
        if (Double.isNaN(d)) {
            String nan = Lua.formatG(d, 1, false);
            sign = nan.startsWith("-") ? "-" : sign;
            body = "nan";
        } else if (Double.isInfinite(d)) {
            body = "inf";
        } else {
            int p = prec < 0 ? 6 : prec;
            boolean alt = fl.contains("#");
            switch (Character.toLowerCase(conv)) {
                case 'e':
                    body = formatE(ad, p, alt);
                    break;
                case 'f': {
                    // redondeo exacto "al par" como printf de C
                    body = new java.math.BigDecimal(ad).setScale(p, java.math.RoundingMode.HALF_EVEN).toPlainString();
                    if (alt && p == 0) body += ".";
                    break;
                }
                default:
                    body = Lua.formatG(ad, p, alt);
            }
        }
        if (upper) body = body.toUpperCase(Locale.ROOT);
        boolean finite = !Double.isNaN(d) && !Double.isInfinite(d);
        return padNumber(sign, body, width, fl, finite);
    }

    /** printf("%.{p}e") de C para un valor no negativo y finito. */
    private static String formatE(double ad, int p, boolean alt) {
        String mant;
        int exp;
        if (ad == 0) {
            StringBuilder z = new StringBuilder("0");
            for (int i = 0; i < p; i++) z.append('0');
            mant = z.toString();
            exp = 0;
        } else {
            java.math.BigDecimal bd = new java.math.BigDecimal(ad)
                    .round(new java.math.MathContext(p + 1, java.math.RoundingMode.HALF_EVEN));
            exp = bd.precision() - bd.scale() - 1;
            mant = bd.unscaledValue().toString();
            while (mant.length() < p + 1) mant += "0";
            mant = mant.substring(0, p + 1);
        }
        StringBuilder sb = new StringBuilder().append(mant.charAt(0));
        if (p > 0 || alt) sb.append('.');
        sb.append(mant, 1, mant.length());
        sb.append('e').append(exp < 0 ? '-' : '+');
        int ae = Math.abs(exp);
        if (ae < 10) sb.append('0');
        return sb.append(ae).toString();
    }

    private static String hexFloat(double d) {
        if (Double.isNaN(d)) return "nan";
        if (Double.isInfinite(d)) return d > 0 ? "inf" : "-inf";
        String h = Double.toHexString(d); // 0x1.8p1
        h = h.replace("0x1.0p", "0x1p").replace("0x0.0p", "0x0p");
        int pi = h.indexOf('p');
        if (pi >= 0 && h.charAt(pi + 1) != '-') h = h.substring(0, pi + 1) + "+" + h.substring(pi + 1);
        return h;
    }

    private static String quote(Object v) {
        if (v instanceof String) {
            String s = (String) v;
            StringBuilder sb = new StringBuilder("\"");
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '"' || c == '\\') {
                    sb.append('\\').append(c);
                } else if (c == '\n') {
                    sb.append("\\\n");
                } else if (c == '\r') {
                    sb.append("\\r");
                } else if (c == 0) {
                    boolean digitNext = i + 1 < s.length() && Character.isDigit(s.charAt(i + 1));
                    sb.append(digitNext ? "\\000" : "\\0");
                } else if (c < 32 || c == 127) {
                    boolean digitNext = i + 1 < s.length() && Character.isDigit(s.charAt(i + 1));
                    sb.append('\\').append(digitNext ? String.format("%03d", (int) c) : Integer.toString(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.append('"').toString();
        }
        if (v instanceof Long) {
            long n = (Long) v;
            return n == Long.MIN_VALUE ? "0x8000000000000000" : Long.toString(n);
        }
        if (v instanceof Double) {
            double d = (Double) v;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return Lua.doubleToLong(d) != null ? String.format(Locale.ROOT, "%dE0", (long) d) : hexFloat(d);
            }
            if (Double.isInfinite(d)) return d > 0 ? "1e9999" : "-1e9999";
            if (Double.isNaN(d)) return "(0/0)";
            return hexFloat(d);
        }
        if (v == null || v instanceof Boolean) return Lua.tostring(v);
        throw new LuaError("bad argument to 'format' (value has no literal form)", true);
    }

    // ------------------------------------------------------------------ table

    private static Object tget(LuaTable t, long i) {
        return t.metatable == null ? t.get(i) : Lua.index(t, i);
    }

    private static void tset(LuaTable t, long i, Object v) {
        if (t.metatable == null) t.put(i, v);
        else Lua.setIndex(t, i, v);
    }

    private static long tlen(LuaTable t) {
        if (t.metatable == null) return t.length();
        Object n = Lua.length(t);
        if (!(n instanceof Long)) throw new LuaError("object length is not an integer", true);
        return (Long) n;
    }

    private static LuaTable installTable() {
        LuaTable T = new LuaTable();
        reg(T, "insert", a -> {
            LuaTable t = checkTable(a, 0, "insert");
            long e = tlen(t) + 1;
            if (a.length == 2) {
                tset(t, e, a[1]);
                return Lua.NONE;
            }
            if (a.length != 3) throw new LuaError("wrong number of arguments to 'insert'", true);
            long pos = checkInt(a, 1, "insert");
            if (Long.compareUnsigned(pos - 1, e) >= 0) throw argError(1, "insert", "position out of bounds");
            if (t.metatable == null && t.arraySize() == e - 1 && a[2] != null) {
                t.insertArray((int) pos, a[2]);
                return Lua.NONE;
            }
            for (long i = e; i > pos; i--) tset(t, i, tget(t, i - 1));
            tset(t, pos, a[2]);
            return Lua.NONE;
        });
        reg(T, "remove", a -> {
            LuaTable t = checkTable(a, 0, "remove");
            long size = tlen(t);
            long pos = optInt(a, 1, "remove", size);
            if (a.length > 1 && pos != size && Long.compareUnsigned(pos - 1, size) > 0) {
                throw argError(1, "remove", "position out of bounds");
            }
            if (t.metatable == null && size == t.arraySize() && pos >= 1 && pos <= size) {
                return ret(t.removeArray((int) pos));
            }
            Object v = tget(t, pos);
            for (; pos < size; pos++) tset(t, pos, tget(t, pos + 1));
            if (pos >= 1 || size > 0 || a.length > 1) tset(t, pos, null);
            return ret(v);
        });
        reg(T, "concat", a -> {
            LuaTable t = checkTable(a, 0, "concat");
            String sep = optString(a, 1, "concat", "");
            long i = optInt(a, 2, "concat", 1);
            long j = arg(a, 3) == null ? tlen(t) : checkInt(a, 3, "concat");
            StringBuilder sb = new StringBuilder();
            for (long k = i; k <= j; k++) {
                Object v = tget(t, k);
                if (!(v instanceof String || v instanceof Long || v instanceof Double)) {
                    throw new LuaError("invalid value (at index " + k + ") in table for 'concat'", true);
                }
                sb.append(Lua.tostring(v));
                if (k < j) sb.append(sep);
            }
            return ret(sb.toString());
        });
        reg(T, "unpack", a -> {
            LuaTable t = checkTable(a, 0, "unpack");
            long i = optInt(a, 1, "unpack", 1);
            long j = arg(a, 2) == null ? tlen(t) : checkInt(a, 2, "unpack");
            if (i > j) return Lua.NONE;
            if (j - i >= 1_000_000) throw new LuaError("too many results to unpack", true);
            Object[] r = new Object[(int) (j - i + 1)];
            for (int k = 0; k < r.length; k++) r[k] = tget(t, i + k);
            return r;
        });
        reg(T, "pack", a -> {
            LuaTable t = new LuaTable();
            for (int i = 0; i < a.length; i++) t.setArrayRaw(i + 1, a[i]);
            t.trimArray();
            t.put("n", (long) a.length);
            return ret(t);
        });
        reg(T, "sort", a -> {
            LuaTable t = checkTable(a, 0, "sort");
            long n = tlen(t);
            if (n > 1) {
                if (n >= Integer.MAX_VALUE) throw argError(0, "sort", "array too big");
                Object comp = arg(a, 1);
                if (comp != null && !(comp instanceof LuaFunction)) throw typeError(a, 1, "sort", "function");
                Object[] arr = new Object[(int) n + 1];
                for (int i = 1; i <= n; i++) arr[i] = tget(t, i);
                new Sorter(arr, comp).auxsort(1, (int) n, 0);
                for (int i = 1; i <= n; i++) tset(t, i, arr[i]);
            }
            return Lua.NONE;
        });
        reg(T, "move", a -> {
            LuaTable a1 = checkTable(a, 0, "move");
            long f = checkInt(a, 1, "move");
            long e = checkInt(a, 2, "move");
            long t = checkInt(a, 3, "move");
            LuaTable tt = arg(a, 4) == null ? a1 : checkTable(a, 4, "move");
            if (e >= f) {
                if (t > e || t <= f || tt != a1) {
                    for (long i = 0; i <= e - f; i++) tset(tt, t + i, tget(a1, f + i));
                } else {
                    for (long i = e - f; i >= 0; i--) tset(tt, t + i, tget(a1, f + i));
                }
            }
            return ret(tt);
        });
        return T;
    }

    /** Port del quicksort de ltablib.c (mismo orden de comparaciones que Lua). */
    private static final class Sorter {
        final Object[] a;
        final Object comp;

        Sorter(Object[] a, Object comp) {
            this.a = a;
            this.comp = comp;
        }

        boolean lt(Object x, Object y) {
            if (comp == null) return Lua.lessThan(x, y);
            return Lua.isTruthy(Lua.first(Lua.call(comp, x, y)));
        }

        void swap(int i, int j) {
            Object t = a[i];
            a[i] = a[j];
            a[j] = t;
        }

        void auxsort(int lo, int up, int rnd) {
            while (lo < up) {
                if (lt(a[up], a[lo])) swap(lo, up);
                if (up - lo == 1) break;
                int p;
                if (up - lo < 100 || rnd == 0) p = (lo + up) >>> 1;
                else {
                    int r4 = (up - lo) / 4;
                    p = Integer.remainderUnsigned(rnd, r4 * 2) + (lo + r4);
                }
                if (lt(a[p], a[lo])) swap(p, lo);
                else if (lt(a[up], a[p])) swap(p, up);
                if (up - lo == 2) break;
                Object pivot = a[p];
                swap(p, up - 1);
                // particion
                int i = lo, j = up - 1;
                while (true) {
                    while (lt(a[++i], pivot)) {
                        if (i == up - 1) throw new LuaError("invalid order function for sorting", true);
                    }
                    while (lt(pivot, a[--j])) {
                        if (j < i) throw new LuaError("invalid order function for sorting", true);
                    }
                    if (j < i) break;
                    swap(i, j);
                }
                swap(up - 1, i);
                p = i;
                int n;
                if (p - lo < up - p) {
                    auxsort(lo, p - 1, rnd);
                    n = p - lo;
                    lo = p + 1;
                } else {
                    auxsort(p + 1, up, rnd);
                    n = up - p;
                    up = p - 1;
                }
                if ((up - lo) / 128 > n) rnd = 0x5bd1e995 ^ (up * 31 + lo);
            }
        }
    }

    // ------------------------------------------------------------------ math

    // xoshiro256** (mismo generador que Lua 5.4)
    private static final long[] RS = new long[4];

    private static long nextRand() {
        long s0 = RS[0], s1 = RS[1], s2 = RS[2] ^ s0, s3 = RS[3] ^ s1;
        long res = Long.rotateLeft(s1 * 5, 7) * 9;
        s1 <<= 17;
        RS[0] = s0 ^ s3;
        RS[1] = RS[1] ^ s2;
        RS[2] = s2 ^ s1;
        RS[3] = Long.rotateLeft(s3, 45);
        return res;
    }

    private static void setSeed(long n1, long n2) {
        RS[0] = n1;
        RS[1] = 0xff;
        RS[2] = n2;
        RS[3] = 0;
        for (int i = 0; i < 16; i++) nextRand();
    }

    private static long project(long ran, long n) {
        if ((n & (n + 1)) == 0) return ran & n;
        long lim = n;
        lim |= (lim >>> 1);
        lim |= (lim >>> 2);
        lim |= (lim >>> 4);
        lim |= (lim >>> 8);
        lim |= (lim >>> 16);
        lim |= (lim >>> 32);
        while (Long.compareUnsigned(ran &= lim, n) > 0) ran = nextRand();
        return ran;
    }

    private static Object floorResult(double d) {
        Long l = Lua.doubleToLong(d);
        return l != null ? (Object) l : (Object) d;
    }

    private static LuaTable installMath() {
        LuaTable M = new LuaTable();
        setSeed(System.nanoTime(), System.identityHashCode(M));
        M.put("pi", Math.PI);
        M.put("huge", Double.POSITIVE_INFINITY);
        M.put("maxinteger", Long.MAX_VALUE);
        M.put("mininteger", Long.MIN_VALUE);
        reg(M, "floor", a -> {
            Object n = checkNumber(a, 0, "floor");
            if (n instanceof Long) return ret(n);
            return ret(floorResult(Math.floor((Double) n)));
        });
        reg(M, "ceil", a -> {
            Object n = checkNumber(a, 0, "ceil");
            if (n instanceof Long) return ret(n);
            return ret(floorResult(Math.ceil((Double) n)));
        });
        reg(M, "abs", a -> {
            Object n = checkNumber(a, 0, "abs");
            if (n instanceof Long) return ret(Math.abs((Long) n));
            return ret(Math.abs((Double) n));
        });
        reg(M, "max", a -> {
            Object m = checkNumber(a, 0, "max");
            for (int i = 1; i < a.length; i++) {
                Object v = checkNumber(a, i, "max");
                if (Lua.numLT(m, v)) m = v;
            }
            return ret(m);
        });
        reg(M, "min", a -> {
            Object m = checkNumber(a, 0, "min");
            for (int i = 1; i < a.length; i++) {
                Object v = checkNumber(a, i, "min");
                if (Lua.numLT(v, m)) m = v;
            }
            return ret(m);
        });
        reg(M, "sqrt", a -> ret(Math.sqrt(checkDouble(a, 0, "sqrt"))));
        reg(M, "exp", a -> ret(Math.exp(checkDouble(a, 0, "exp"))));
        reg(M, "sin", a -> ret(Math.sin(checkDouble(a, 0, "sin"))));
        reg(M, "cos", a -> ret(Math.cos(checkDouble(a, 0, "cos"))));
        reg(M, "tan", a -> ret(Math.tan(checkDouble(a, 0, "tan"))));
        reg(M, "asin", a -> ret(Math.asin(checkDouble(a, 0, "asin"))));
        reg(M, "acos", a -> ret(Math.acos(checkDouble(a, 0, "acos"))));
        reg(M, "atan", a -> ret(Math.atan2(checkDouble(a, 0, "atan"), arg(a, 1) == null ? 1.0 : checkDouble(a, 1, "atan"))));
        reg(M, "log", a -> {
            double x = checkDouble(a, 0, "log");
            if (arg(a, 1) == null) return ret(Math.log(x));
            double b = checkDouble(a, 1, "log");
            if (b == 2.0) {
                double r = Math.log(x) / Math.log(2);
                double rr = Math.rint(r);
                if (Math.abs(r - rr) < 1e-12 && Math.pow(2, rr) == x) r = rr;
                return ret(r);
            }
            if (b == 10.0) return ret(Math.log10(x));
            return ret(Math.log(x) / Math.log(b));
        });
        reg(M, "fmod", a -> {
            Object x = checkNumber(a, 0, "fmod"), y = checkNumber(a, 1, "fmod");
            if (x instanceof Long && y instanceof Long) {
                long d = (Long) y;
                if (d == 0) throw argError(1, "fmod", "zero");
                if (d == -1) return ret(0L);
                return ret((Long) x % d);
            }
            return ret(Lua.toDouble(x) % Lua.toDouble(y));
        });
        reg(M, "modf", a -> {
            Object x = checkNumber(a, 0, "modf");
            if (x instanceof Long) return new Object[] {x, 0.0};
            double d = (Double) x;
            double ip = d >= 0 ? Math.floor(d) : Math.ceil(d);
            double frac = d == ip ? 0.0 : d - ip;
            return new Object[] {floorResult(ip), frac};
        });
        reg(M, "tointeger", a -> {
            Object v = arg(a, 0);
            Long l = (v instanceof Long || v instanceof Double || v instanceof String) ? Lua.toInteger(v) : null;
            if (l == null) checkAny(a, 0, "tointeger");
            return ret(l);
        });
        reg(M, "type", a -> {
            checkAny(a, 0, "type");
            Object v = a[0];
            if (v instanceof Long) return ret("integer");
            if (v instanceof Double) return ret("float");
            return NIL;
        });
        reg(M, "ult", a -> ret(Long.compareUnsigned(checkInt(a, 0, "ult"), checkInt(a, 1, "ult")) < 0));
        reg(M, "random", a -> {
            long rv = nextRand();
            long low, up;
            if (a.length == 0) return ret((rv >>> 11) * 0x1.0p-53);
            if (a.length == 1) {
                low = 1;
                up = checkInt(a, 0, "random");
                if (up == 0) return ret(rv);
            } else {
                low = checkInt(a, 0, "random");
                up = checkInt(a, 1, "random");
            }
            if (low > up) throw argError(a.length == 1 ? 0 : 1, "random", "interval is empty");
            return ret(project(rv, up - low) + low);
        });
        reg(M, "randomseed", a -> {
            if (a.length == 0) {
                setSeed(System.nanoTime(), System.currentTimeMillis());
                return Lua.NONE;
            }
            Object n = checkNumber(a, 0, "randomseed");
            long n1 = n instanceof Long ? (Long) n : (long) (double) (Double) n;
            long n2 = optInt(a, 1, "randomseed", 0);
            setSeed(n1, n2);
            return new Object[] {n1, n2};
        });
        return M;
    }

    // ------------------------------------------------------------------ os

    private static LuaTable installOs() {
        LuaTable O = new LuaTable();
        reg(O, "time", a -> {
            Object t = arg(a, 0);
            if (t == null) return ret(System.currentTimeMillis() / 1000);
            LuaTable tt = checkTable(a, 0, "time");
            int year = (int) fieldInt(tt, "year", -1);
            int month = (int) fieldInt(tt, "month", -1);
            int day = (int) fieldInt(tt, "day", -1);
            long hour = fieldInt(tt, "hour", 12), min = fieldInt(tt, "min", 0), sec = fieldInt(tt, "sec", 0);
            LocalDateTime ldt = LocalDateTime.of(year, 1, 1, 0, 0).plusMonths(month - 1).plusDays(day - 1)
                    .plusHours(hour).plusMinutes(min).plusSeconds(sec);
            return ret(ldt.atZone(ZoneId.systemDefault()).toEpochSecond());
        });
        reg(O, "clock", a -> ret((System.nanoTime() - START) / 1e9));
        reg(O, "date", a -> {
            String fmt = optString(a, 0, "date", "%c");
            long t = arg(a, 1) == null ? System.currentTimeMillis() / 1000 : checkInt(a, 1, "date");
            ZoneId zone = ZoneId.systemDefault();
            if (fmt.startsWith("!")) {
                zone = ZoneId.of("UTC");
                fmt = fmt.substring(1);
            }
            ZonedDateTime d = Instant.ofEpochSecond(t).atZone(zone);
            if (fmt.startsWith("*t")) {
                LuaTable r = new LuaTable();
                r.put("year", (long) d.getYear());
                r.put("month", (long) d.getMonthValue());
                r.put("day", (long) d.getDayOfMonth());
                r.put("hour", (long) d.getHour());
                r.put("min", (long) d.getMinute());
                r.put("sec", (long) d.getSecond());
                r.put("wday", (long) (d.getDayOfWeek().getValue() % 7 + 1));
                r.put("yday", (long) d.getDayOfYear());
                r.put("isdst", Boolean.FALSE);
                return ret(r);
            }
            return ret(strftime(fmt, d));
        });
        reg(O, "getenv", a -> ret(System.getenv(checkString(a, 0, "getenv"))));
        reg(O, "exit", a -> {
            out.flush();
            Object c = arg(a, 0);
            int code = c == null || Boolean.TRUE.equals(c) ? 0 : c instanceof Long ? (int) (long) (Long) c : 1;
            System.exit(code);
            return Lua.NONE;
        });
        reg(O, "remove", a -> new Object[] {null, checkString(a, 0, "remove") + ": operation not supported"});
        reg(O, "rename", a -> new Object[] {null, "operation not supported"});
        reg(O, "tmpname", a -> ret("/tmp/lua_tmp"));
        return O;
    }

    private static long fieldInt(LuaTable t, String k, long def) {
        Object v = t.get(k);
        if (v == null) {
            if (def < 0) throw new LuaError("field '" + k + "' missing in date table", true);
            return def;
        }
        Long l = Lua.toInteger(v);
        if (l == null) throw new LuaError("field '" + k + "' is not an integer", true);
        return l;
    }

    private static String two(int v) {
        return v < 10 ? "0" + v : Integer.toString(v);
    }

    private static String strftime(String fmt, ZonedDateTime d) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fmt.length(); i++) {
            char c = fmt.charAt(i);
            if (c != '%' || i + 1 >= fmt.length()) {
                sb.append(c);
                continue;
            }
            char f = fmt.charAt(++i);
            switch (f) {
                case 'Y': sb.append(d.getYear()); break;
                case 'y': sb.append(two(d.getYear() % 100)); break;
                case 'm': sb.append(two(d.getMonthValue())); break;
                case 'd': sb.append(two(d.getDayOfMonth())); break;
                case 'H': sb.append(two(d.getHour())); break;
                case 'I': sb.append(two((d.getHour() + 11) % 12 + 1)); break;
                case 'M': sb.append(two(d.getMinute())); break;
                case 'S': sb.append(two(d.getSecond())); break;
                case 'p': sb.append(d.getHour() < 12 ? "AM" : "PM"); break;
                case 'A': sb.append(d.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)); break;
                case 'a': sb.append(d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)); break;
                case 'B': sb.append(d.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)); break;
                case 'b': sb.append(d.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)); break;
                case 'j': sb.append(String.format("%03d", d.getDayOfYear())); break;
                case 'x': sb.append(two(d.getMonthValue())).append('/').append(two(d.getDayOfMonth()))
                        .append('/').append(two(d.getYear() % 100)); break;
                case 'X': sb.append(two(d.getHour())).append(':').append(two(d.getMinute()))
                        .append(':').append(two(d.getSecond())); break;
                case 'c': sb.append(strftime("%a %b %d %H:%M:%S %Y", d)); break;
                case '%': sb.append('%'); break;
                default: sb.append('%').append(f);
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ io

    private static LuaTable installIo() {
        LuaTable IO = new LuaTable();
        LuaTable fileMethods = new LuaTable();
        LuaTable fileMeta = new LuaTable();
        fileMeta.put("__index", fileMethods);
        fileMeta.put("__name", "FILE*");
        LuaUserdata stdout = new LuaUserdata(fileMeta);
        LuaUserdata stderr = new LuaUserdata(fileMeta);
        fileMeta.put("__tostring", LuaFunction.of("tostring", a -> ret("file (" + Lua.address(a[0]) + ")")));
        reg(fileMethods, "write", a -> {
            PrintStream ps = arg(a, 0) == stderr ? System.err : out;
            if (ps == System.err) out.flush();
            for (int i = 1; i < a.length; i++) ps.print(writeArg(a, i));
            return ret(a.length > 0 ? a[0] : null);
        });
        reg(fileMethods, "flush", a -> {
            out.flush();
            return ret(arg(a, 0));
        });
        reg(fileMethods, "close", a -> ret(Boolean.TRUE));
        reg(fileMethods, "setvbuf", a -> ret(Boolean.TRUE));
        reg(fileMethods, "read", a -> NIL);
        reg(fileMethods, "lines", a -> ret(LuaFunction.of("lines_iter", x -> NIL)));
        IO.put("stdout", stdout);
        IO.put("stderr", stderr);
        IO.put("stdin", new LuaUserdata(fileMeta));
        reg(IO, "write", a -> {
            for (int i = 0; i < a.length; i++) out.print(writeArg(a, i));
            return ret(stdout);
        });
        reg(IO, "read", a -> NIL);
        reg(IO, "lines", a -> ret(LuaFunction.of("lines_iter", x -> NIL)));
        reg(IO, "flush", a -> {
            out.flush();
            return Lua.NONE;
        });
        return IO;
    }

    private static String writeArg(Object[] a, int i) {
        Object v = a[i];
        if (v instanceof String) return (String) v;
        if (v instanceof Long) return Lua.tostring(v);
        if (v instanceof Double) return Lua.formatG((Double) v, 14, false); // LUAI_NUMFFORMAT "%.14g"
        throw typeError(a, i, "write", "string");
    }

    // ------------------------------------------------------------------ coroutine

    private static final Coroutine MAIN = new Coroutine(null, null);

    private static Coroutine checkCo(Object[] a, int i, String fname) {
        Object v = arg(a, i);
        if (v instanceof Coroutine) return (Coroutine) v;
        throw typeError(a, i, fname, "coroutine");
    }

    private static LuaTable installCoroutine() {
        LuaTable C = new LuaTable();
        MAIN.status = "running";
        reg(C, "create", a -> {
            Object f = arg(a, 0);
            if (!(f instanceof LuaFunction)) throw typeError(a, 0, "create", "function");
            return ret(new Coroutine(I, f));
        });
        reg(C, "resume", a -> {
            Coroutine co = checkCo(a, 0, "resume");
            return co.resume(Arrays.copyOfRange(a, 1, a.length));
        });
        reg(C, "yield", a -> {
            Coroutine co = I.currentCoroutine;
            if (co == null) throw new LuaError("attempt to yield from outside a coroutine", true);
            return co.yield(a);
        });
        reg(C, "status", a -> {
            Coroutine co = checkCo(a, 0, "status");
            if (co == MAIN) return ret(I.currentCoroutine == null ? "running" : "normal");
            return ret(co.status);
        });
        reg(C, "isyieldable", a -> ret(I.currentCoroutine != null));
        reg(C, "running", a -> {
            Coroutine co = I.currentCoroutine;
            return co == null ? new Object[] {MAIN, Boolean.TRUE} : new Object[] {co, Boolean.FALSE};
        });
        reg(C, "wrap", a -> {
            Object f = arg(a, 0);
            if (!(f instanceof LuaFunction)) throw typeError(a, 0, "wrap", "function");
            Coroutine co = new Coroutine(I, f);
            return ret(LuaFunction.of("wrap", x -> {
                Object[] r = co.resume(x);
                if (r[0] == Boolean.FALSE) {
                    Object err = r.length > 1 ? r[1] : null;
                    throw new LuaError(err);
                }
                return Arrays.copyOfRange(r, 1, r.length);
            }));
        });
        reg(C, "close", a -> {
            Coroutine co = checkCo(a, 0, "close");
            if ("suspended".equals(co.status) || "dead".equals(co.status)) {
                co.status = "dead";
                return ret(Boolean.TRUE);
            }
            throw new LuaError("cannot close a " + co.status + " coroutine", true);
        });
        return C;
    }

    // ------------------------------------------------------------------ utf8

    private static LuaTable installUtf8() {
        LuaTable U = new LuaTable();
        U.put("charpattern", "[\u0000-\u007FÂ-ý][\u0080-¿]*");
        reg(U, "char", a -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length; i++) Interpreter.appendUtf8(sb, checkInt(a, i, "char"));
            return ret(sb.toString());
        });
        reg(U, "len", a -> {
            String s = checkString(a, 0, "len");
            long i = posrelat(optInt(a, 1, "len", 1), s.length());
            long j = endpos(optInt(a, 2, "len", -1), s.length());
            long n = 0;
            int p = (int) i - 1;
            while (p < j) {
                int[] dec = decodeUtf8(s, p);
                if (dec == null) return new Object[] {null, (long) p + 1};
                p = dec[1];
                n++;
            }
            return ret(n);
        });
        reg(U, "codepoint", a -> {
            String s = checkString(a, 0, "codepoint");
            long i = posrelat(optInt(a, 1, "codepoint", 1), s.length());
            long j = endpos(optInt(a, 2, "codepoint", i), s.length());
            java.util.ArrayList<Object> r = new java.util.ArrayList<>();
            int p = (int) i - 1;
            while (p < j) {
                int[] dec = decodeUtf8(s, p);
                if (dec == null) throw new LuaError("invalid UTF-8 code", true);
                r.add((long) dec[0]);
                p = dec[1];
            }
            return r.toArray();
        });
        reg(U, "offset", a -> {
            String s = checkString(a, 0, "offset");
            long n = checkInt(a, 1, "offset");
            long i = optInt(a, 2, "offset", n >= 0 ? 1 : s.length() + 1);
            int p = (int) posrelat(i, s.length()) - 1;
            if (n > 0) {
                n--;
                while (n > 0 && p < s.length()) {
                    p++;
                    while (p < s.length() && (s.charAt(p) & 0xC0) == 0x80) p++;
                    n--;
                }
                if (n == 0) return ret((long) p + 1);
                return NIL;
            } else if (n < 0) {
                while (n < 0 && p > 0) {
                    p--;
                    while (p > 0 && (s.charAt(p) & 0xC0) == 0x80) p--;
                    n++;
                }
                if (n == 0) return ret((long) p + 1);
                return NIL;
            }
            while (p > 0 && p < s.length() && (s.charAt(p) & 0xC0) == 0x80) p--;
            return ret((long) p + 1);
        });
        reg(U, "codes", a -> {
            String s = checkString(a, 0, "codes");
            return new Object[] {LuaFunction.of("codes_iter", x -> {
                long i = (Long) x[1];
                int p = (int) i - 1;
                if (p >= 0) {
                    p++;
                    while (p < s.length() && (s.charAt(p) & 0xC0) == 0x80) p++;
                } else {
                    p = 0;
                }
                if (p >= s.length()) return NIL;
                int[] dec = decodeUtf8(s, p);
                if (dec == null) throw new LuaError("invalid UTF-8 code", true);
                return new Object[] {(long) p + 1, (long) dec[0]};
            }), s, 0L};
        });
        return U;
    }

    /** Decodifica un caracter UTF-8 en la posicion p: {codigo, siguiente posicion} o null. */
    static int[] decodeUtf8(String s, int p) {
        int c = s.charAt(p) & 0xFF;
        if (c < 0x80) return new int[] {c, p + 1};
        int n;
        int cp;
        if (c >= 0xF0) { n = 3; cp = c & 0x07; }
        else if (c >= 0xE0) { n = 2; cp = c & 0x0F; }
        else if (c >= 0xC0) { n = 1; cp = c & 0x1F; }
        else return null;
        if (p + n >= s.length() + 0 && p + n > s.length() - 1 + 1) return null;
        for (int k = 1; k <= n; k++) {
            if (p + k >= s.length()) return null;
            int cc = s.charAt(p + k) & 0xFF;
            if ((cc & 0xC0) != 0x80) return null;
            cp = (cp << 6) | (cc & 0x3F);
        }
        return new int[] {cp, p + n + 1};
    }
}
