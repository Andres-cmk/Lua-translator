import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tabla de Lua: parte arreglo (claves enteras 1..n) + parte hash que conserva
 * el orden de insercion (asi pairs() recorre en un orden predecible).
 */
public final class LuaTable {
    private static final Object[] EMPTY = new Object[0];

    // Parte arreglo: arr[i] guarda la clave i+1. asize es el numero de casillas en uso.
    private Object[] arr = EMPTY;
    private int asize = 0;

    // Parte hash con orden de insercion. Un valor null es una "lapida" (clave borrada):
    // la clave se conserva para que next() siga funcionando durante un recorrido.
    private final ArrayList<Object> hkeys = new ArrayList<>();
    private final ArrayList<Object> hvals = new ArrayList<>();
    private final HashMap<Object, Integer> hindex = new HashMap<>();
    private int hlive = 0;

    public LuaTable metatable;

    public LuaTable() {}

    public LuaTable(int narr, int nhash) {
        if (narr > 0) arr = new Object[narr];
    }

    /** Las claves float con valor entero se guardan como enteros (2.0 y 2 son la misma clave). */
    static Object normalizeKey(Object key) {
        if (key instanceof Double) {
            double d = (Double) key;
            if (d == Math.rint(d) && d >= -9.223372036854775808E18 && d < 9.223372036854775808E18) {
                return (long) d;
            }
        }
        return key;
    }

    public Object get(Object key) {
        if (key instanceof Long) {
            long k = (Long) key;
            if (k >= 1 && k <= asize) return arr[(int) (k - 1)];
        } else if (key instanceof Double) {
            key = normalizeKey(key);
            if (key instanceof Long) return get(key);
        } else if (key == null) {
            return null;
        }
        Integer idx = hindex.get(key);
        return idx == null ? null : hvals.get(idx);
    }

    public Object get(long k) {
        if (k >= 1 && k <= asize) return arr[(int) (k - 1)];
        if (hlive == 0) return null;
        Integer idx = hindex.get(k);
        return idx == null ? null : hvals.get(idx);
    }

    public Object get(String key) {
        if (hlive == 0) return null;
        Integer idx = hindex.get(key);
        return idx == null ? null : hvals.get(idx);
    }

    /** Asignacion cruda (sin metametodos). */
    public void put(Object key, Object val) {
        if (key == null) throw new LuaError("table index is nil", true);
        if (key instanceof Double) {
            double d = (Double) key;
            if (Double.isNaN(d)) throw new LuaError("table index is NaN", true);
            key = normalizeKey(key);
        }
        if (key instanceof Long) {
            putInt((Long) key, val);
            return;
        }
        putHash(key, val);
    }

    public void put(long k, Object val) {
        putInt(k, val);
    }

    private void putInt(long k, Object val) {
        if (k >= 1 && k <= asize) {
            arr[(int) (k - 1)] = val;
            if (val == null && k == asize) {
                while (asize > 0 && arr[asize - 1] == null) asize--;
            }
            return;
        }
        if (k == asize + 1L && val != null && asize < Integer.MAX_VALUE - 8) {
            // Si la clave estaba en el hash, se quita de ahi.
            if (hlive > 0) removeHash(k);
            appendArray(val);
            // Mover al arreglo las claves consecutivas que esten en el hash.
            while (hlive > 0) {
                Long next = (long) asize + 1;
                Integer idx = hindex.get(next);
                if (idx == null) break;
                Object v = hvals.get(idx);
                if (v == null) break;
                removeHash(next);
                appendArray(v);
            }
            return;
        }
        putHash(k, val);
    }

    private void appendArray(Object val) {
        if (asize == arr.length) {
            int ncap = Math.max(4, arr.length * 2);
            Object[] na = new Object[ncap];
            System.arraycopy(arr, 0, na, 0, asize);
            arr = na;
        }
        arr[asize++] = val;
    }

    private void removeHash(Object key) {
        Integer idx = hindex.get(key);
        if (idx != null && hvals.get(idx) != null) {
            hvals.set(idx, null);
            hlive--;
        }
    }

    private void putHash(Object key, Object val) {
        Integer idx = hindex.get(key);
        if (idx != null) {
            Object old = hvals.get(idx);
            hvals.set(idx, val);
            if (old == null && val != null) hlive++;
            else if (old != null && val == null) hlive--;
            return;
        }
        if (val == null) return;
        if (hkeys.size() > 16 && hlive * 2 < hkeys.size()) compact();
        hindex.put(key, hkeys.size());
        hkeys.add(key);
        hvals.add(val);
        hlive++;
    }

    /** Elimina lapidas. Solo ocurre al insertar claves nuevas (igual que el rehash de Lua). */
    private void compact() {
        ArrayList<Object> ks = new ArrayList<>();
        ArrayList<Object> vs = new ArrayList<>();
        for (int i = 0; i < hkeys.size(); i++) {
            if (hvals.get(i) != null) {
                ks.add(hkeys.get(i));
                vs.add(hvals.get(i));
            }
        }
        hkeys.clear();
        hvals.clear();
        hindex.clear();
        for (int i = 0; i < ks.size(); i++) {
            hindex.put(ks.get(i), i);
            hkeys.add(ks.get(i));
            hvals.add(vs.get(i));
        }
    }

    /** Operador # (un "borde" de la tabla). */
    public long length() {
        if (asize > 0) return asize;
        if (hlive == 0) return 0;
        // Sin parte arreglo: buscar el borde en el hash.
        long n = 0;
        while (get(n + 1) != null) n++;
        return n;
    }

    /**
     * next(t, k): devuelve {clave, valor} siguiente a key, o null al terminar.
     */
    public Object[] next(Object key) {
        int ai;
        int hi;
        if (key == null) {
            ai = 0;
            hi = 0;
        } else {
            key = normalizeKey(key);
            if (key instanceof Long && (Long) key >= 1 && (Long) key <= asize) {
                ai = (int) (long) (Long) key;
                hi = 0;
            } else {
                Integer idx = hindex.get(key);
                if (idx != null) {
                    ai = asize;
                    hi = idx + 1;
                } else if (key instanceof Long && (Long) key > asize) {
                    // la clave quedo fuera del arreglo porque se borro durante el recorrido
                    ai = asize;
                    hi = 0;
                } else {
                    throw new LuaError("invalid key to 'next'", true);
                }
            }
        }
        for (int i = ai; i < asize; i++) {
            if (arr[i] != null) return new Object[] {(long) (i + 1), arr[i]};
        }
        for (int i = hi; i < hkeys.size(); i++) {
            Object v = hvals.get(i);
            if (v != null) return new Object[] {hkeys.get(i), v};
        }
        return null;
    }

    /** Usado por el constructor de tablas: agrega al arreglo aunque el valor sea nil. */
    void setArrayRaw(int i, Object v) {
        // i es 1-based y llega en orden creciente
        while (asize < i - 1) appendArray(null);
        if (asize == i - 1) appendArray(v);
        else arr[i - 1] = v;
    }

    void trimArray() {
        while (asize > 0 && arr[asize - 1] == null) asize--;
    }

    int arraySize() {
        return asize;
    }

    /** Inserta en la posicion pos (1..n+1) corriendo los elementos (table.insert rapido). */
    void insertArray(int pos, Object v) {
        appendArray(null);
        System.arraycopy(arr, pos - 1, arr, pos, asize - pos);
        arr[pos - 1] = v;
    }

    /** Quita la posicion pos (1..n) corriendo los elementos (table.remove rapido). */
    Object removeArray(int pos) {
        Object v = arr[pos - 1];
        System.arraycopy(arr, pos, arr, pos - 1, asize - pos);
        arr[--asize] = null;
        trimArray();
        return v;
    }
}
