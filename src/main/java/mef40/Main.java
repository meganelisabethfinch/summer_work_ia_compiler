package mef40;

import mef40.lexer.Lexer;
import mef40.parser.Parser;

import java.io.EOFException;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please input one expression.");
            return;
        }

        try {
            String input = args[0];
            Queue<Token> tokens = Lexer.lex(input);

            // output lexer result
            System.out.println("Token stream produced by lexer:");
            System.out.println(tokens);

            ParseTreeNode parseTree = Parser.parse(tokens);

            // output parser result
            System.out.println("Parse tree produced by parser:");
            System.out.println(parseTree);
        } catch (EOFException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
