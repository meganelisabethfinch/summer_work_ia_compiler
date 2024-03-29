package mef40.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import mef40.*;
import mef40.grammar.NonTerminal;
import mef40.grammar.Terminal;

import java.util.*;

public class Parser {
    // The grammar for my parser
    public static final ImmutableList<Production> grammar = ImmutableList.of(
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
            new Production(NonTerminal.STATEMENT, NonTerminal.FLOAT),
            new Production(NonTerminal.FLOAT, Terminal.MINUS, Terminal.UFLOAT),
            new Production(NonTerminal.FLOAT, Terminal.UFLOAT)
    );

    private static final ParsingTable table = new ParsingTable(grammar);

    /**
     * ...
     *
     * @param tokens - input string w
     *
     * @return a parse tree
     */
    public static ParseTreeNode parse(Queue<Token> tokens) throws IllegalArgumentException {
        Queue<Token> w$ = new LinkedList<>(tokens);
        w$.add(new Token(Terminal.$));

        Stack<ImmutableSet<Item>> states = new Stack<>();
        states.add(table.initialState);

        Stack<ParseTreeNode> nodes = new Stack<>();
        // List<Production> reductionSteps = new ArrayList();

        var a = w$.poll().tag;
        while (true) {
            var s = states.peek();
            var action = table.ACTION(s, a);

            if (action == null) {
                throw new IllegalArgumentException("Not able to parse the input string.");
            } else if (action.getClass().equals(Shift.class)) {
                var state = ((Shift) action).state;

                states.push(state);
                nodes.push(new ParseTreeNode(a));

                a = w$.poll().tag;
            } else if (action.getClass().equals(Reduce.class)) {
                var production = ((Reduce) action).production;

                List<ParseTreeNode> children = new ArrayList<>();

                for (int i = 0; i < production.size(); i++) {
                    states.pop();
                    children.add(nodes.pop());
                }

                Collections.reverse(children);

                states.push(table.GOTO(states.peek(), production.head));
                nodes.push(new ParseTreeNode(production.head, children));

                // reductionSteps.add(production);
            } else if (action.getClass().equals(Accept.class)) {
                break;
            }
        }

        return nodes.pop();
    }
}
