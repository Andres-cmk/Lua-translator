import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Semantica de los valores de Lua 5.4.
 *
 * Representacion en Java:
 *   nil -> null, boolean -> Boolean, integer -> Long, float -> Double,
 *   string -> String (un char por byte, ISO-8859-1), table -> LuaTable,
 *   function -> LuaFunction, thread -> Coroutine.
 */
public final class Lua {
    private Lua() {}

    public static final Object[] NONE = new Object[0];
    public static LuaTable stringMeta;

    // Codigos de operacion
    static final int ADD = 0, SUB = 1, MUL = 2, MOD = 3, POW = 4, DIV = 5, IDIV = 6,
            BAND = 7, BOR = 8, BXOR = 9, SHL = 10, SHR = 11, UNM = 12, BNOT = 13;
    static final String[] EVENTS = {"__add", "__sub", "__mul", "__mod", "__pow", "__div", "__idiv",
            "__band", "__bor", "__bxor", "__shl", "__shr", "__unm", "__bnot"};

    // ---------------------------------------------------------------- tipos

    public static String typeName(Object v) {
        if (v == null) return "nil";
        if (v instanceof Boolean) return "boolean";
        if (v instanceof Long || v instanceof Double) return "number";
        if (v instanceof String) return "string";
        if (v instanceof LuaTable) return "table";
        if (v instanceof LuaFunction) return "function";
        if (v instanceof Coroutine) return "thread";
        return "userdata";
    }

    public static boolean isTruthy(Object v) {
        return v != null && v != Boolean.FALSE && !(v instanceof Boolean && !((Boolean) v));
    }

    public static Object first(Object[] vs) {
        return vs.length == 0 ? null : vs[0];
    }

    public static LuaTable getMetatable(Object v) {
        if (v instanceof LuaTable) return ((LuaTable) v).metatable;
        if (v instanceof String) return stringMeta;
        return null;
    }

    public static Object metaOf(Object v, String event) {
        LuaTable mt = getMetatable(v);
        return mt == null ? null : mt.get(event);
    }

    // ---------------------------------------------------------------- llamadas

    public static Object[] call(Object f, Object... args) {
        if (f instanceof LuaFunction) return ((LuaFunction) f).call(args);
        Object h = metaOf(f, "__call");
        if (h != null) {
            Object[] nargs = new Object[args.length + 1];
            nargs[0] = f;
            System.arraycopy(args, 0, nargs, 1, args.length);
            return call(h, nargs);
        }
        LuaError e = new LuaError("attempt to call a " + typeName(f) + " value", true);
        e.describeCallee = true;
        throw e;
    }

    // ---------------------------------------------------------------- indexacion

    public static Object index(Object o, Object key) {
        for (int loop = 0; loop < 2000; loop++) {
            Object h;
            if (o instanceof LuaTable) {
                LuaTable t = (LuaTable) o;
                Object v = t.get(key);
                if (v != null) return v;
                if (t.metatable == null) return null;
                h = t.metatable.get("__index");
                if (h == null) return null;
            } else {
                h = metaOf(o, "__index");
                if (h == null) {
                    throw LuaError.operand("attempt to index a " + typeName(o) + " value", 0);
                }
            }
            if (h instanceof LuaFunction) return first(((LuaFunction) h).call(new Object[] {o, key}));
            o = h;
        }
        throw new LuaError("'__index' chain too long; possible loop", true);
    }

    public static void setIndex(Object o, Object key, Object val) {
        for (int loop = 0; loop < 2000; loop++) {
            Object h;
            if (o instanceof LuaTable) {
                LuaTable t = (LuaTable) o;
                if (t.metatable == null) {
                    t.put(key, val);
                    return;
                }
                if (t.get(key) != null) {
                    t.put(key, val);
                    return;
                }
                h = t.metatable.get("__newindex");
                if (h == null) {
                    t.put(key, val);
                    return;
                }
            } else {
                h = metaOf(o, "__newindex");
                if (h == null) {
                    throw LuaError.operand("attempt to index a " + typeName(o) + " value", 0);
                }
            }
            if (h instanceof LuaFunction) {
                ((LuaFunction) h).call(new Object[] {o, key, val});
                return;
            }
            o = h;
        }
        throw new LuaError("'__newindex' chain too long; possible loop", true);
    }

    // ---------------------------------------------------------------- conversiones

    /** Convierte a numero para aritmetica (los strings se convierten como en Lua). */
    public static Object toNumber(Object v) {
        if (v instanceof Long || v instanceof Double) return v;
        if (v instanceof String) return str2number((String) v);
        return null;
    }

    /** Convierte a entero (para operaciones bitwise, indices, etc). null si no se puede. */
    public static Long toInteger(Object v) {
        if (v instanceof Long) return (Long) v;
        if (v instanceof Double) return doubleToLong((Double) v);
        if (v instanceof String) {
            Object n = str2number((String) v);
            return n == null ? null : toInteger(n);
        }
        return null;
    }

    public static Long doubleToLong(double d) {
        if (d == Math.floor(d) && d >= -9.223372036854775808E18 && d < 9.223372036854775808E18) {
            return (long) d;
        }
        return null;
    }

    public static double toDouble(Object n) {
        return n instanceof Long ? (double) (Long) n : (Double) n;
    }

    private static boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == 0x0B;
    }

    /** Conversion string -> numero segun las reglas lexicas de Lua. null si no es numero. */
    public static Object str2number(String s) {
        int i = 0, n = s.length();
        while (i < n && isSpace(s.charAt(i))) i++;
        while (n > i && isSpace(s.charAt(n - 1))) n--;
        if (i >= n) return null;
        String t = s.substring(i, n);
        boolean neg = false;
        int p = 0;
        if (t.charAt(0) == '-' || t.charAt(0) == '+') {
            neg = t.charAt(0) == '-';
            p = 1;
        }
        if (t.length() > p + 1 && t.charAt(p) == '0' && (t.charAt(p + 1) == 'x' || t.charAt(p + 1) == 'X')) {
            Object r = parseHex(t, p + 2);
            if (r == null) return null;
            if (r instanceof Long) return neg ? -(Long) r : r;
            return neg ? -(Double) r : r;
        }
        // decimal
        int j = p;
        int digits = 0;
        boolean isFloat = false;
        while (j < t.length() && Character.isDigit(t.charAt(j)) && t.charAt(j) < 128) { j++; digits++; }
        if (j < t.length() && t.charAt(j) == '.') {
            isFloat = true;
            j++;
            while (j < t.length() && t.charAt(j) >= '0' && t.charAt(j) <= '9') { j++; digits++; }
        }
        if (digits == 0) return null;
        if (j < t.length() && (t.charAt(j) == 'e' || t.charAt(j) == 'E')) {
            isFloat = true;
            j++;
            if (j < t.length() && (t.charAt(j) == '+' || t.charAt(j) == '-')) j++;
            int ed = 0;
            while (j < t.length() && t.charAt(j) >= '0' && t.charAt(j) <= '9') { j++; ed++; }
            if (ed == 0) return null;
        }
        if (j != t.length()) return null;
        if (!isFloat) {
            try {
                return Long.parseLong(t);
            } catch (NumberFormatException e) {
                // no cabe en un entero: se vuelve float
            }
        }
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int hexVal(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'F') return c - 'A' + 10;
        return -1;
    }

    private static Object parseHex(String t, int p) {
        long mant = 0;
        double fmant = 0;
        int exp = 0;
        boolean any = false, isFloat = false;
        int i = p;
        while (i < t.length() && hexVal(t.charAt(i)) >= 0) {
            mant = (mant << 4) | hexVal(t.charAt(i));
            fmant = fmant * 16 + hexVal(t.charAt(i));
            i++;
            any = true;
        }
        if (i < t.length() && t.charAt(i) == '.') {
            isFloat = true;
            i++;
            while (i < t.length() && hexVal(t.charAt(i)) >= 0) {
                fmant = fmant * 16 + hexVal(t.charAt(i));
                exp -= 4;
                i++;
                any = true;
            }
        }
        if (!any) return null;
        if (i < t.length() && (t.charAt(i) == 'p' || t.charAt(i) == 'P')) {
            isFloat = true;
            i++;
            boolean eneg = false;
            if (i < t.length() && (t.charAt(i) == '+' || t.charAt(i) == '-')) {
                eneg = t.charAt(i) == '-';
                i++;
            }
            int e = 0, ed = 0;
            while (i < t.length() && t.charAt(i) >= '0' && t.charAt(i) <= '9') {
                e = Math.min(e * 10 + (t.charAt(i) - '0'), 100000);
                i++;
                ed++;
            }
            if (ed == 0) return null;
            exp += eneg ? -e : e;
        }
        if (i != t.length()) return null;
        if (!isFloat) return mant; // los enteros hexadecimales "dan la vuelta" (modulo 2^64)
        return fmant * Math.pow(2, exp);
    }

    // ---------------------------------------------------------------- tostring

    public static String tostring(Object v) {
        if (v == null) return "nil";
        if (v instanceof String) return (String) v;
        if (v instanceof Long) return Long.toString((Long) v);
        if (v instanceof Double) return numberToString((Double) v);
        if (v instanceof Boolean) return ((Boolean) v) ? "true" : "false";
        Object h = metaOf(v, "__tostring");
        if (h != null) {
            Object r = first(call(h, v));
            if (!(r instanceof String)) {
                if (r instanceof Long || r instanceof Double) return tostring(r);
                throw new LuaError("'__tostring' must return a string", true);
            }
            return (String) r;
        }
        Object name = metaOf(v, "__name");
        String kind = name instanceof String ? (String) name : typeName(v);
        return kind + ": " + address(v);
    }

    static String address(Object v) {
        long h = System.identityHashCode(v) & 0xffffffffL;
        return String.format("0x%014x", 0x55550000000000L + (h << 4));
    }

    /** Formato "%.14g" que usa Lua para los floats, agregando ".0" si parece entero. */
    public static String numberToString(double d) {
        String s = formatG(d, 14, false);
        if (s.indexOf('.') < 0 && s.indexOf('e') < 0 && s.indexOf('n') < 0) s += ".0";
        return s;
    }

    /** Equivalente a printf("%.{prec}g") de C. */
    public static String formatG(double d, int prec, boolean alt) {
        // En x86 el NaN que produce C (0/0) es negativo y Lua imprime "-nan"; en Java el
        // NaN canonico es positivo, asi que el signo se interpreta al reves.
        if (Double.isNaN(d)) return Double.doubleToRawLongBits(d) < 0 ? "nan" : "-nan";
        if (Double.isInfinite(d)) return d > 0 ? "inf" : "-inf";
        if (prec == 0) prec = 1;
        if (d == 0) {
            String z = (1 / d < 0) ? "-0" : "0";
            if (alt) {
                StringBuilder sb = new StringBuilder(z).append('.');
                for (int i = 1; i < prec; i++) sb.append('0');
                return sb.toString();
            }
            return z;
        }
        BigDecimal bd = new BigDecimal(d).round(new MathContext(prec, RoundingMode.HALF_EVEN));
        int exp = bd.precision() - bd.scale() - 1;
        if (exp < -4 || exp >= prec) {
            // notacion cientifica con prec-1 decimales
            String digits = bd.unscaledValue().abs().toString();
            while (digits.length() < prec) digits += "0";
            StringBuilder sb = new StringBuilder();
            if (bd.signum() < 0) sb.append('-');
            sb.append(digits.charAt(0));
            String frac = digits.substring(1, prec);
            if (!alt) frac = stripZeros(frac);
            if (frac.length() > 0 || alt) sb.append('.').append(frac);
            sb.append('e').append(exp < 0 ? '-' : '+');
            int ae = Math.abs(exp);
            if (ae < 10) sb.append('0');
            sb.append(ae);
            return sb.toString();
        }
        int decimals = Math.max(0, prec - 1 - exp);
        String s = bd.setScale(decimals, RoundingMode.HALF_EVEN).toPlainString();
        if (!alt && s.indexOf('.') >= 0) {
            s = stripZeros(s);
            if (s.endsWith(".")) s = s.substring(0, s.length() - 1);
        } else if (alt && s.indexOf('.') < 0) {
            s += ".";
        }
        return s;
    }

    private static String stripZeros(String s) {
        int e = s.length();
        while (e > 0 && s.charAt(e - 1) == '0') e--;
        return s.substring(0, e);
    }

    // ---------------------------------------------------------------- aritmetica

    static final String[] OPNAMES = {"add", "sub", "mul", "mod", "pow", "div", "idiv",
            "band", "bor", "bxor", "shl", "shr", "unm", "bnot"};

    public static Object arith(int op, Object a, Object b) {
        if (op >= BAND && op <= SHR || op == BNOT) return bitwise(op, a, b);
        Object na = toNumber(a), nb = toNumber(b);
        if (na != null && nb != null) return arithRaw(op, na, nb);
        // En Lua 5.4 la coercion de strings la hacen los metametodos de la libreria string.
        if (a instanceof String) return stringArith(op, a, b);
        Object h = metaOf(a, EVENTS[op]);
        if (h != null) return first(call(h, a, b));
        if (b instanceof String) return stringArith(op, a, b);
        h = metaOf(b, EVENTS[op]);
        if (h != null) return first(call(h, a, b));
        int culprit = (na == null) ? 0 : 1;
        Object bad = culprit == 0 ? a : b;
        throw LuaError.operand("attempt to perform arithmetic on a " + typeName(bad) + " value", culprit);
    }

    /** Metametodo aritmetico de los strings (lstrlib.c: trymt). */
    private static Object stringArith(int op, Object a, Object b) {
        Object h = b instanceof String ? null : metaOf(b, EVENTS[op]);
        if (h == null) {
            throw new LuaError("attempt to " + OPNAMES[op] + " a '" + typeName(a) + "' with a '"
                    + typeName(b) + "'", true);
        }
        return first(call(h, a, b));
    }

    static Object arithRaw(int op, Object a, Object b) {
        if (a instanceof Long && b instanceof Long) {
            long x = (Long) a, y = (Long) b;
            switch (op) {
                case ADD: return x + y;
                case SUB: return x - y;
                case MUL: return x * y;
                case MOD:
                    if (y == 0) throw new LuaError("attempt to perform 'n%0'", true);
                    return Math.floorMod(x, y);
                case IDIV:
                    if (y == 0) throw new LuaError("attempt to divide by zero", true);
                    return Math.floorDiv(x, y);
                case UNM: return -x;
                case DIV: return (double) x / (double) y;
                case POW: return Math.pow(x, y);
                default: break;
            }
        }
        double x = toDouble(a), y = toDouble(b);
        switch (op) {
            case ADD: return x + y;
            case SUB: return x - y;
            case MUL: return x * y;
            case DIV: return x / y;
            case POW: return pow(x, y);
            case UNM: return -x;
            case IDIV: return Math.floor(x / y);
            case MOD: {
                double m;
                if (Double.isInfinite(y) && !Double.isNaN(x) && !Double.isInfinite(x)) m = x;
                else m = x % y;
                if ((m > 0) ? y < 0 : (m < 0 && y != m)) m += y;
                return m;
            }
            default: throw new LuaError("bad arithmetic op", true);
        }
    }

    private static double pow(double x, double y) {
        if (y == 2) return x * x;
        if (y == 0.5) return Math.sqrt(x);
        return Math.pow(x, y);
    }

    static Object bitwise(int op, Object a, Object b) {
        Long ia = bitOperand(a), ib = (op == BNOT) ? ia : bitOperand(b);
        if (ia != null && ib != null) {
            long x = ia, y = ib;
            switch (op) {
                case BAND: return x & y;
                case BOR: return x | y;
                case BXOR: return x ^ y;
                case SHL: return shiftLeft(x, y);
                case SHR: return shiftLeft(x, -y);
                case BNOT: return ~x;
                default: break;
            }
        }
        Object h = metaOf(a, EVENTS[op]);
        if (h == null) h = metaOf(b, EVENTS[op]);
        if (h != null) return first(call(h, a, b));
        int culprit = (ia == null) ? 0 : 1;
        Object bad = culprit == 0 ? a : b;
        if (bad instanceof Double) {
            throw LuaError.operand("number has no integer representation", culprit);
        }
        throw LuaError.operand("attempt to perform bitwise operation on a " + typeName(bad) + " value", culprit);
    }

    private static Long bitOperand(Object v) {
        if (v instanceof Long) return (Long) v;
        if (v instanceof Double) return doubleToLong((Double) v);
        return null; // en Lua 5.4 los strings no se convierten en operaciones bitwise
    }

    static long shiftLeft(long x, long y) {
        if (y <= -64 || y >= 64) return 0;
        if (y >= 0) return x << y;
        return x >>> -y;
    }

    // ---------------------------------------------------------------- comparacion

    public static boolean rawEquals(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a instanceof Long) {
            if (b instanceof Long) return ((Long) a).longValue() == (Long) b;
            if (b instanceof Double) {
                Long bl = doubleToLong((Double) b);
                return bl != null && bl.longValue() == (Long) a;
            }
            return false;
        }
        if (a instanceof Double) {
            if (b instanceof Double) return ((Double) a).doubleValue() == (Double) b;
            if (b instanceof Long) return rawEquals(b, a);
            return false;
        }
        if (a instanceof String || a instanceof Boolean) return a.equals(b);
        return false;
    }

    public static boolean equals(Object a, Object b) {
        if (rawEquals(a, b)) return true;
        if (a instanceof LuaTable && b instanceof LuaTable) {
            Object h = metaOf(a, "__eq");
            if (h == null) h = metaOf(b, "__eq");
            if (h != null) return isTruthy(first(call(h, a, b)));
        }
        return false;
    }

    public static boolean lessThan(Object a, Object b) {
        if ((a instanceof Long || a instanceof Double) && (b instanceof Long || b instanceof Double)) {
            return numLT(a, b);
        }
        if (a instanceof String && b instanceof String) return ((String) a).compareTo((String) b) < 0;
        Object h = metaOf(a, "__lt");
        if (h == null) h = metaOf(b, "__lt");
        if (h != null) return isTruthy(first(call(h, a, b)));
        throw compareError(a, b);
    }

    public static boolean lessEqual(Object a, Object b) {
        if ((a instanceof Long || a instanceof Double) && (b instanceof Long || b instanceof Double)) {
            return numLE(a, b);
        }
        if (a instanceof String && b instanceof String) return ((String) a).compareTo((String) b) <= 0;
        Object h = metaOf(a, "__le");
        if (h == null) h = metaOf(b, "__le");
        if (h != null) return isTruthy(first(call(h, a, b)));
        throw compareError(a, b);
    }

    private static LuaError compareError(Object a, Object b) {
        String t1 = typeName(a), t2 = typeName(b);
        if (t1.equals(t2)) return new LuaError("attempt to compare two " + t1 + " values", true);
        return new LuaError("attempt to compare " + t1 + " with " + t2, true);
    }

    static boolean numLT(Object a, Object b) {
        if (a instanceof Long && b instanceof Long) return (Long) a < (Long) b;
        if (a instanceof Double && b instanceof Double) return (Double) a < (Double) b;
        if (a instanceof Long) return intLTFloat((Long) a, (Double) b);
        return floatLTInt((Double) a, (Long) b);
    }

    static boolean numLE(Object a, Object b) {
        if (a instanceof Long && b instanceof Long) return (Long) a <= (Long) b;
        if (a instanceof Double && b instanceof Double) return (Double) a <= (Double) b;
        if (a instanceof Long) return !Double.isNaN((Double) b) && !floatLTInt((Double) b, (Long) a);
        return !Double.isNaN((Double) a) && !intLTFloat((Long) b, (Double) a);
    }

    private static final double TWO63 = 9.223372036854775808E18;

    private static boolean intLTFloat(long i, double f) {
        if (Double.isNaN(f)) return false;
        if (f >= TWO63) return true;
        if (f < -TWO63) return false;
        double c = Math.ceil(f);
        if (c >= TWO63) return true;
        return i < (long) c;
    }

    private static boolean floatLTInt(double f, long i) {
        if (Double.isNaN(f)) return false;
        if (f >= TWO63) return false;
        if (f < -TWO63) return true;
        double fl = Math.floor(f);
        return (long) fl < i;
    }

    // ---------------------------------------------------------------- concat y longitud

    public static Object concat(Object a, Object b) {
        if ((a instanceof String || a instanceof Long || a instanceof Double)
                && (b instanceof String || b instanceof Long || b instanceof Double)) {
            return tostring(a).concat(tostring(b));
        }
        Object h = metaOf(a, "__concat");
        if (h == null) h = metaOf(b, "__concat");
        if (h != null) return first(call(h, a, b));
        int culprit = (a instanceof String || a instanceof Long || a instanceof Double) ? 1 : 0;
        Object bad = culprit == 0 ? a : b;
        throw LuaError.operand("attempt to concatenate a " + typeName(bad) + " value", culprit);
    }

    public static Object length(Object v) {
        if (v instanceof String) return (long) ((String) v).length();
        Object h = metaOf(v, "__len");
        if (h != null) return first(call(h, v));
        if (v instanceof LuaTable) return ((LuaTable) v).length();
        throw LuaError.operand("attempt to get length of a " + typeName(v) + " value", 0);
    }
}
