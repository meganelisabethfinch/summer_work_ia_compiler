package mef40.lexer;

import mef40.Terminal;
import mef40.Token;
import mef40.UFloat;

import java.io.EOFException;
import java.util.LinkedList;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Queue;

public class Lexer {
    private static final Map<Character, Terminal> symbolLookup = Map.ofEntries(
            entry('+', Terminal.PLUS),
            entry('-', Terminal.MINUS),
            entry('*', Terminal.MULT),
            entry('!', Terminal.FACTORIAL),
            entry('(', Terminal.OPEN),
            entry(')', Terminal.CLOSE)
    );

    public static Queue<Token> lex(String input) throws EOFException {
        var queue = new LinkedList<Token>();
        var reader = new LexerReader(input.replaceAll("\\s+", ""));
        while (reader.peek() != '$') {
            var next = getToken(reader);
            queue.add(next);
        }

        return queue;
    }

    private static Token getToken(LexerReader reader) throws EOFException {
        int state = 1;
        char c = '$';

        while (true) {
            switch (state) {
                case 1:
                    c = reader.read();

                    if (Character.isDigit(c)) {
                        state = 2;
                    } else if (c == 'c') {
                        state = 7;
                    } else if ("+-!()*".indexOf(c) != -1) {
                        state = 10;
                    } else {
                        state = 15;
                    }
                    break;
                case 2:
                    c = reader.read();

                    if (Character.isDigit(c)) {
                        break;
                    } else if(c == '.') {
                        state = 3;
                    } else if (c == 'e') {
                        state = 11;
                    } else {
                        state = 5;
                    }
                    break;
                case 3:
                    c = reader.read();

                    if (Character.isDigit(c)) {
                        state = 4;
                    } else {
                        state = 15;
                    }
                    break;
                case 4:
                    c = reader.read();

                    if (Character.isDigit(c)) {
                        break;
                    } else if (c == 'e') {
                        state = 11;
                    } else {
                        state = 5;
                    }
                    break;
                case 5:
                    reader.retract();
                    return new UFloat(Float.parseFloat(reader.consumeLexeme()));
                case 7:
                    c = reader.read();

                    if (c == 'o') {
                        state = 8;
                    } else {
                        state = 15;
                    }
                    break;
                case 8:
                    c = reader.read();

                    if (c == 's') {
                        state = 9;
                    } else {
                        state = 15;
                    }
                    break;
                case 9:
                    reader.consumeLexeme();
                    return new Token(Terminal.COS);
                case 10:
                    var lexeme = reader.consumeLexeme().charAt(0);
                    return new Token(symbolLookup.get(lexeme));
                case 11:
                    c = reader.read();

                    if (Character.isDigit(c)) {
                        state = 13;
                    } else if (c == '+' || c == '-') {
                        state = 12;
                    } else {
                        state = 15;
                    }
                    break;
                case 12:
                    c = reader.read();

                    if (Character.isDigit(c)) {
                        state = 13;
                    } else {
                        state = 15;
                    }
                    break;
                case 13:
                    c = reader.read();

                    if (Character.isDigit(c)) {
                        break;
                    } else {
                        state = 5;
                    }
                    break;
                case 15:
                    // Dead state
                    if (c == '$') {
                        throw new EOFException("Failed to form valid lexeme before reaching end of input.");
                    }
                    throw new IllegalArgumentException("Failed to form valid lexeme on character: " + c);
            }
        }

    }
}
