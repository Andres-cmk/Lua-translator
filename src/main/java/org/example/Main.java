package org.example;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.example.LuaLexer;
import org.example.LuaGrammar;

public class Main {
    public static void main(String[] args) {
        try {
            CharStream input;
            if (args.length > 0) {
                input = CharStreams.fromFileName(args[0]);
            } else {
                input = CharStreams.fromStream(System.in);
            }
            LuaLexer lexer = new LuaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LuaGrammar parser = new LuaGrammar(tokens);
            ParseTree tree = parser.chunk();
            System.out.println(tree.toStringTree(parser));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
