package mef40.parser;

import mef40.NonTerminal;
import mef40.Terminal;
import mef40.Token;

import java.util.*;

public class Parser {
    // The grammar for my parser
    private static final List<Production> grammar = List.of(
            new Production(NonTerminal.START, NonTerminal.EXPR),
            new Production(NonTerminal.EXPR, NonTerminal.EXPR, Terminal.PLUS, NonTerminal.DIFF),
            new Production(NonTerminal.EXPR, NonTerminal.DIFF),
            new Production(NonTerminal.DIFF, NonTerminal.DIFF, Terminal.MINUS, NonTerminal.PROD),
            new Production(NonTerminal.DIFF, NonTerminal.PROD),
            new Production(NonTerminal.PROD, NonTerminal.OPTCOS, Terminal.MULT, NonTerminal.PROD),
            new Production(NonTerminal.PROD, NonTerminal.OPTCOS),
            new Production(NonTerminal.OPTCOS, Terminal.COS, NonTerminal.OPTCOS),
            new Production(NonTerminal.OPTCOS, NonTerminal.OPTFACT),
            new Production(NonTerminal.OPTFACT, NonTerminal.OPTFACT, Terminal.FACTORIAL),
            new Production(NonTerminal.OPTFACT, NonTerminal.STATEMENT),
            new Production(NonTerminal.STATEMENT, Terminal.OPEN, NonTerminal.EXPR, Terminal.CLOSE),
            new Production(NonTerminal.STATEMENT, NonTerminal.FLOAT)
    );

    private static final ParsingTable table = new ParsingTable(grammar);

    /**
     * ...
     *
     * @param tokens - input string w
     *
     * @return list of 'reduction steps' (i.e. productions by which we reduce)
     */
    public static List<Production> parse(Queue<Token> tokens) {
        Queue<Token> w$ = new LinkedList<>(tokens);
        w$.add(new Token(Terminal.$));

        List<Production> out = new ArrayList();
        Stack<Set<Item>> states = new Stack<>();

        var a = w$.poll();
        while (true) {
            var s = states.peek(); // INIT STACK WITH SOMETHING?

            Action action = table.ACTION(s, a);
            if (action.getClass().equals(Shift.class)) {

            }

        }


    }
}
