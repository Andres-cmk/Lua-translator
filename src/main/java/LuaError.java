/**
 * Error de Lua. 'value' es el objeto de error (normalmente un string).
 * Si 'needsPosition' es true, el interprete le antepone "stdin:LINEA:" en el
 * sitio donde ocurrio, como hace Lua con los errores de ejecucion.
 * 'culprit' indica que operando causo el error (0 = izquierdo/unico, 1 = derecho)
 * para poder agregar el nombre de la variable, p.ej. "(local 'x')".
 */
public final class LuaError extends RuntimeException {
    public Object value;
    public boolean needsPosition;
    public int culprit = -1;
    public boolean describeCallee;

    public LuaError(Object value) {
        this(value, false);
    }

    public LuaError(Object value, boolean needsPosition) {
        super(null, null, false, false);
        this.value = value;
        this.needsPosition = needsPosition;
    }

    static LuaError operand(String msg, int culprit) {
        LuaError e = new LuaError(msg, true);
        e.culprit = culprit;
        return e;
    }

    @Override
    public String getMessage() {
        if (value instanceof String) return (String) value;
        if (value == null) return "nil";
        if (value instanceof LuaTable) {
            Object ts = Lua.metaOf(value, "__tostring");
            if (ts != null) return Lua.tostring(value);
            return "(error object is a " + Lua.typeName(value) + " value)";
        }
        return Lua.tostring(value);
    }
}
