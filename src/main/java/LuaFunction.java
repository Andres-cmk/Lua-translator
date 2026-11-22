/** Cualquier valor invocable de Lua: closures definidas en el programa y funciones de la libreria. */
public abstract class LuaFunction {
    public final String name;
    public String qualifiedName;

    protected LuaFunction(String name) {
        this.name = name;
    }

    public abstract Object[] call(Object[] args);

    /** Interfaz funcional para definir las funciones de la libreria con lambdas. */
    public interface Body {
        Object[] call(Object[] args);
    }

    public static LuaFunction of(String name, Body body) {
        return new LuaFunction(name) {
            @Override
            public Object[] call(Object[] args) {
                return body.call(args);
            }
        };
    }
}
