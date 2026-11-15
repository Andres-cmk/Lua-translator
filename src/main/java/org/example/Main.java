package org.example;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;
import org.example.LuaLexer;
import org.example.LuaGrammar;

public class Main {
    public static void main(String[] args) {
        try {
            LuaLexer lexer;
            if (args.length > 0) {
                lexer = new LuaLexer(CharStreams.fromFileName(args[0]));
            } else {
                lexer = new LuaLexer(CharStreams.fromStream(System.in));
            }
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            LuaGrammar parser = new LuaGrammar(tokenStream);
            ParseTree tree = parser.chunk();
            System.out.println(tree.toStringTree(parser));
        }catch (Exception e){
            System.out.println(e.getCause().getMessage());
        }
    }
}