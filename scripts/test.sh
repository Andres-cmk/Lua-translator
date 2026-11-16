#!/usr/bin/env bash
# Ejecuta cada caso de tests/casos/*.lua y compara la salida con tests/esperado/*.txt
# (las salidas esperadas se obtuvieron con Lua 5.4 oficial).
# Las direcciones de memoria (table: 0x...) se ignoran porque cambian en cada ejecucion.
#
#   ./scripts/test.sh            (compila primero si hace falta)
set -uo pipefail
cd "$(dirname "$0")/.."

RUNTIME=lib/antlr-runtime-4.9.3.jar
[ -f build/classes/Main.class ] || ./scripts/build.sh > /dev/null

norm() { sed -E 's/0x[0-9a-fA-F]+/0xADDR/g'; }

pass=0
fail=0
for caso in tests/casos/*.lua; do
    nombre=$(basename "$caso" .lua)
    esperado="tests/esperado/$nombre.txt"
    obtenido=$(java -cp "build/classes:$RUNTIME" Main < "$caso" 2>&1 | grep -v '^Picked up JAVA_TOOL_OPTIONS' | norm)
    if [ "$obtenido" == "$(norm < "$esperado")" ]; then
        pass=$((pass + 1))
        echo "OK    $nombre"
    else
        fail=$((fail + 1))
        echo "FALLA $nombre"
        diff <(norm < "$esperado") <(echo "$obtenido") | head -20
    fi
done
echo
echo "$pass correctos, $fail con diferencias"
[ "$fail" -eq 0 ]
