# Intérprete de Lua en Java (ANTLR 4)

Práctica 3 de **Lenguajes de Programación** (Universidad Nacional de Colombia, 2026-2):
analizador semántico de Lua, en la modalidad de **intérprete**.

## Integrantes

| Nombre | Usuario de GitHub |
|---|---|
| Andrés Felipe Ramírez | [@Andres-cmk](https://github.com/Andres-cmk) |
| Luisa Bautista | [@mbautistaa02](https://github.com/mbautistaa02) |
| Misael Jesús Flórez Anave | [@misaeljesus](https://github.com/misaeljesus) |

## Qué hace

El programa lee por la **entrada estándar** el código fuente de un programa en Lua 5.4,
lo analiza con un lexer y un parser generados por ANTLR y lo **ejecuta**, recorriendo el
árbol sintáctico con el patrón *visitor* (como en el tutorial de intérpretes del curso).

La salida es la misma que produciría el intérprete oficial de Lua 5.4. Por ejemplo, para el
caso de prueba de `input/entrada.txt`:

```
1. Elemento posicional via IIFE y bitwise left shift:	40

2. Clave dinamica calculada -> (2^3 + '10.5'//2) & 0xFF:	13
...
```

Si el programa Lua lanza un error que nadie atrapa, se imprime `lua: <mensaje>` en la salida
de error y el proceso termina normalmente (código 0), para no producir `RUNTIME_ERROR`.

## Cobertura de Lua 5.4

- **Tipos:** nil, booleanos, enteros de 64 bits y floats (con la distinción de Lua 5.4),
  strings, tablas, funciones y corrutinas.
- **Expresiones:** todos los operadores con la precedencia y asociatividad oficiales
  (`^` y `..` por la derecha), aritmética entera/float (`//`, `%`, `^`), operadores bitwise
  (`& | ~ << >> ~`), comparaciones, `and`/`or` en cortocircuito, longitud `#`,
  concatenación y coerción string↔número como en Lua 5.4.
- **Sentencias:** asignación múltiple, `local` (incluye `<const>`), `if/elseif/else`, `while`,
  `repeat/until`, `for` numérico y genérico, `break`, `goto`/etiquetas, `do ... end`, `return`.
- **Funciones:** closures con upvalues, varargs (`...`, `select`), retornos múltiples,
  métodos con `:`, recursión profunda y llamadas de cola.
- **Metatablas:** `__index`, `__newindex`, `__call`, `__tostring`, `__name`, `__len`,
  `__eq`, `__lt`, `__le`, `__concat`, `__unm`, aritméticos y bitwise, `__metatable`, `__pairs`.
- **Variables `<close>`:** se llama `__close` al salir del bloque, también cuando hay un error.
- **Tablas débiles:** `__mode` y `collectgarbage("collect")` eliminan las entradas inalcanzables.
- **Errores:** `error`, `pcall`, `xpcall`, `assert`, con los mismos mensajes que Lua
  (por ejemplo `stdin:3: attempt to index a nil value (local 't')`).
- **Librería estándar:** `print`, `type`, `tostring`, `tonumber`, `pairs`, `ipairs`, `next`,
  `select`, `rawget/rawset/rawequal/rawlen`, `setmetatable/getmetatable`, `load`;
  `string` (incluye patrones de Lua: `find`, `match`, `gmatch`, `gsub`, y `format`),
  `table` (`insert`, `remove`, `concat`, `sort`, `unpack`, `pack`, `move`),
  `math` (incluye `random` con el mismo generador xoshiro256** de Lua 5.4), `os`, `io.write`,
  `coroutine` y `utf8`.

## Estructura

```
grammar/LuaLexer.g4       reglas léxicas
grammar/LuaGrammar.g4     gramática (con etiquetas # para el visitor y precedencia de Lua 5.4)
src/main/java/
  Main.java               lee stdin, parsea y ejecuta
  Interpreter.java        el visitor que ejecuta el árbol (sentencias, expresiones, llamadas)
  Lua.java                semántica de los valores (aritmética, comparación, coerción, tostring)
  LuaTable.java           tablas (parte arreglo + parte hash con orden de inserción)
  LuaFunction.java        valores invocables
  LuaError.java           errores de Lua
  LuaPattern.java         patrones de Lua (port de lstrlib.c)
  StdLib.java             librería estándar
  Coroutine.java          corrutinas (un hilo por corrutina, uno solo corre a la vez)
  LuaUserdata.java        valores userdata (archivos de io)
  GarbageCollector.java   limpieza de tablas débiles en collectgarbage()
lib/                      runtime de ANTLR 4.9.3 (va en el zip de UNcode)
tools/                    ANTLR 4.9.3 completo (para generar el parser)
tests/casos/              programas Lua de prueba (uncode_01..06 son los casos oficiales de UNcode)
tests/esperado/           salida de Lua 5.4 oficial para cada caso
scripts/build.sh          genera el parser, compila y arma entrega/uncode.zip
scripts/test.sh           corre todos los casos y los compara con la salida esperada
```

## Compilar, probar y entregar

Requisitos: JDK 8 o superior.

```bash
./scripts/build.sh                                            # genera, compila y arma el zip
java -cp build/classes:lib/antlr-runtime-4.9.3.jar Main < input/entrada.txt
./scripts/test.sh                                             # compara con Lua 5.4 oficial
```

`entrega/uncode.zip` queda con la estructura que pide UNcode para proyectos Java:
`src/` (con `Main.java` y el parser generado, en el paquete por defecto) y `lib/` (runtime de ANTLR).

También se puede compilar con Maven (`mvn package`) o desde IntelliJ como en el tutorial del
curso: generar el reconocedor desde `grammar/LuaGrammar.g4`, marcar `gen` como carpeta de fuentes
y agregar `lib/antlr-runtime-4.9.3.jar` como dependencia.

## Decisiones y detalles

- **Java 8 y ANTLR 4.9.3:** la documentación de UNcode indica JDK 1.8 para proyectos Java, y
  ANTLR 4.10 o superior exige Java 11. Por eso el parser se genera con 4.9.3 y se compila a
  bytecode de Java 8.
- **Strings como bytes:** el código fuente se lee en ISO-8859-1 (un carácter por byte), así
  `#"ñ"` da 2 como en Lua y las tildes se imprimen sin alterarse.
- **Orden de `pairs`:** primero la parte arreglo y luego las demás claves en orden de inserción.
  En Lua oficial el orden de las claves de tipo string es arbitrario y cambia entre ejecuciones.
- **Direcciones de memoria:** `print({})` muestra `table: 0x...` con un valor que, igual que en
  Lua, no es fijo.
- **Pila:** el intérprete corre en un hilo con pila grande para soportar recursión profunda;
  un desborde se reporta como el error de Lua `stack overflow`.
