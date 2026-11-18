import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Recolector para las tablas debiles (metacampo __mode).
 *
 * La memoria la maneja la JVM; lo unico que hay que imitar de Lua es que
 * collectgarbage("collect") saque de las tablas debiles las entradas cuyas
 * claves o valores ya no son alcanzables. Para eso se hace una fase de marcado
 * desde las raices (globales, ambitos activos y corrutinas).
 */
final class GarbageCollector {
    private GarbageCollector() {}

    private static final List<LuaTable> WEAK_TABLES = new ArrayList<>();

    static void register(LuaTable t) {
        if (!WEAK_TABLES.contains(t)) WEAK_TABLES.add(t);
    }

    private static boolean collectable(Object v) {
        return v instanceof LuaTable || v instanceof LuaFunction || v instanceof Coroutine || v instanceof LuaUserdata;
    }

    private static String mode(LuaTable t) {
        if (t.metatable == null) return "";
        Object m = t.metatable.get("__mode");
        return m instanceof String ? (String) m : "";
    }

    static void collect(Interpreter interp) {
        if (WEAK_TABLES.isEmpty()) return;
        IdentityHashMap<Object, Boolean> marked = new IdentityHashMap<>();
        ArrayList<Object> pending = new ArrayList<>();

        // raices
        pending.add(interp.globals);
        pending.add(Lua.stringMeta);
        addState(pending, interp.scope, interp.state);
        for (Coroutine co = interp.currentCoroutine; co != null; co = co.previous()) {
            pending.add(co);
            addState(pending, co.resumerScope, co.resumerState);
        }

        // marcado
        while (!pending.isEmpty()) {
            Object v = pending.remove(pending.size() - 1);
            if (v == null || marked.containsKey(v)) continue;
            if (v instanceof Interpreter.Scope) {
                marked.put(v, Boolean.TRUE);
                Interpreter.Scope s = (Interpreter.Scope) v;
                s.collectValues(pending);
                pending.add(s.parent);
                continue;
            }
            if (!collectable(v)) continue;
            marked.put(v, Boolean.TRUE);
            if (v instanceof LuaTable) {
                LuaTable t = (LuaTable) v;
                pending.add(t.metatable);
                String m = mode(t);
                boolean weakK = m.indexOf('k') >= 0, weakV = m.indexOf('v') >= 0;
                for (Object[] kv = t.next(null); kv != null; kv = t.next(kv[0])) {
                    if (!weakK) pending.add(kv[0]);
                    if (!weakV) pending.add(kv[1]);
                }
            } else if (v instanceof Interpreter.Closure) {
                pending.add(((Interpreter.Closure) v).env);
            } else if (v instanceof Coroutine) {
                Coroutine co = (Coroutine) v;
                pending.add(co.fn);
                addState(pending, co.savedScope(), co.callState());
            } else if (v instanceof LuaUserdata) {
                pending.add(((LuaUserdata) v).metatable);
            }
        }

        // limpieza de las tablas debiles
        for (LuaTable t : WEAK_TABLES) {
            String m = mode(t);
            boolean weakK = m.indexOf('k') >= 0, weakV = m.indexOf('v') >= 0;
            ArrayList<Object> dead = new ArrayList<>();
            for (Object[] kv = t.next(null); kv != null; kv = t.next(kv[0])) {
                boolean deadK = weakK && collectable(kv[0]) && !marked.containsKey(kv[0]);
                boolean deadV = weakV && collectable(kv[1]) && !marked.containsKey(kv[1]);
                if (deadK || deadV) dead.add(kv[0]);
            }
            for (Object k : dead) t.put(k, null);
        }
    }

    private static void addState(List<Object> pending, Interpreter.Scope scope, Interpreter.CallState st) {
        pending.add(scope);
        if (st == null) return;
        for (int i = 0; i < st.depth; i++) pending.add(st.scopes[i]);
    }
}
