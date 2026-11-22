#!/usr/bin/env bash
# Genera el parser con ANTLR, compila el interprete y arma el zip para UNcode.
#
#   ./scripts/build.sh
#
# Resultado:
#   build/classes/        clases compiladas (para probar localmente)
#   entrega/uncode.zip    zip listo para subir (src/ + lib/ + README.md)
set -euo pipefail
cd "$(dirname "$0")/.."

TOOL=tools/antlr-4.9.3-complete.jar
RUNTIME=lib/antlr-runtime-4.9.3.jar

rm -rf build entrega
mkdir -p build/gen build/classes build/uncode/src build/uncode/lib entrega

echo "==> Generando lexer y parser (ANTLR 4.9.3)"
java -jar "$TOOL" -Dlanguage=Java -visitor -no-listener -o build/gen -Xexact-output-dir \
    grammar/LuaLexer.g4 grammar/LuaGrammar.g4

echo "==> Compilando (bytecode Java 8, que es la version de UNcode)"
RELEASE="--release 8"
if javac -version 2>&1 | grep -q '1\.8'; then RELEASE=""; fi
# shellcheck disable=SC2086
javac $RELEASE -nowarn -encoding UTF-8 -cp "$RUNTIME" -d build/classes \
    build/gen/*.java src/main/java/*.java

echo "==> Armando entrega/uncode.zip"
cp build/gen/*.java src/main/java/*.java build/uncode/src/
cp "$RUNTIME" build/uncode/lib/
cp README.md build/uncode/
(cd build/uncode && zip -qr ../../entrega/uncode.zip src lib README.md)

echo "Listo. Para probar:"
echo "  java -cp build/classes:$RUNTIME Main < input/entrada.txt"
