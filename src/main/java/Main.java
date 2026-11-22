import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.PredictionMode;

/**
 * Interprete de Lua 5.4.
 * Lee el programa por la entrada estandar (o de un archivo pasado como argumento)
 * y lo ejecuta, imprimiendo la salida tal como lo haria Lua.
 */
public class Main {

    /** Error de sintaxis (solo se usa para load(); la entrada principal no tiene errores). */
    static final class SyntaxError extends RuntimeException {
        SyntaxError(String msg) {
            super(msg, null, false, false);
        }
    }

    /**
     * Parsea codigo fuente Lua. Los strings se manejan como bytes (ISO-8859-1), asi
     * las tildes y la longitud (#) se comportan igual que en Lua.
     */
    static LuaGrammar.ChunkContext parse(String src) {
        LuaLexer lexer = new LuaLexer(CharStreams.fromString(src));
        BaseErrorListener thrower = new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> r, Object sym, int line, int col, String msg,
                                    RecognitionException e) {
                String near = "?";
                if (sym instanceof Token) {
                    Token t = (Token) sym;
                    near = t.getType() == Token.EOF ? "<eof>" : "'" + t.getText() + "'";
                }
                throw new SyntaxError(line + ": syntax error near " + near);
            }
        };
        lexer.removeErrorListeners();
        lexer.addErrorListener(thrower);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        // Fijar el texto de cada token una sola vez (evita recalcularlo en cada evaluacion).
        for (Token t : tokens.getTokens()) {
            if (t.getType() != Token.EOF && t instanceof CommonToken) {
                ((CommonToken) t).setText(t.getText().intern());
            }
        }
        LuaGrammar parser = new LuaGrammar(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(thrower);
        // Primero el modo rapido (SLL); si falla, el modo completo (LL).
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        try {
            return parser.chunk();
        } catch (SyntaxError e) {
            tokens.seek(0);
            parser.reset();
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            return parser.chunk();
        }
    }

    private static String readAll(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1 << 16];
        int n;
        while ((n = in.read(buf)) > 0) bos.write(buf, 0, n);
        return new String(bos.toByteArray(), StandardCharsets.ISO_8859_1);
    }

    private static void run(String[] args) {
        PrintStream out;
        try {
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out), 1 << 16),
                    false, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            out = System.out;
        }
        StdLib.out = out;
        try {
            String src;
            if (args.length > 0) {
                src = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.ISO_8859_1);
            } else {
                src = readAll(System.in);
            }
            if (src.startsWith("#")) {
                int nl = src.indexOf('\n');
                src = nl < 0 ? "" : "--" + src.substring(nl);
            }
            LuaGrammar.ChunkContext chunk = parse(src);
            Interpreter interp = new Interpreter();
            interp.makeChunk(chunk, "main").call(Lua.NONE);
        } catch (LuaError e) {
            out.flush();
            System.err.println("lua: " + e.getMessage());
        } catch (SyntaxError e) {
            out.flush();
            System.err.println("lua: stdin:" + e.getMessage());
        } catch (StackOverflowError e) {
            out.flush();
            System.err.println("lua: stack overflow");
        } catch (Throwable e) {
            out.flush();
            System.err.println("lua: " + e);
        } finally {
            out.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        // Se ejecuta en un hilo con pila grande para soportar recursion profunda.
        long[] sizes = {1024L << 20, 256L << 20, 64L << 20};
        for (long size : sizes) {
            try {
                Thread t = new Thread(null, () -> run(args), "lua-main", size);
                t.start();
                t.join();
                System.out.flush();
                System.exit(0);
                return;
            } catch (OutOfMemoryError e) {
                // no se pudo reservar esa pila: probar con una mas pequena
            }
        }
        run(args);
        System.exit(0);
    }
}
