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
            System.out.println(tokens);
            Node parseTree = Parser.parse(tokens);
            System.out.println(parseTree);
        } catch (EOFException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
