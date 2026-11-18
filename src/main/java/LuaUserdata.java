/** Valor de tipo userdata (se usa para los archivos de la libreria io, p.ej. io.stdout). */
public final class LuaUserdata {
    public LuaTable metatable;

    public LuaUserdata(LuaTable metatable) {
        this.metatable = metatable;
    }
}
