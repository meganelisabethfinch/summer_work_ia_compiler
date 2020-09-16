package mef40.parser;

import com.google.common.collect.ImmutableSet;
import mef40.NonTerminal;
import mef40.Symbol;
import mef40.Terminal;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParsingTable {
    public final ImmutableSet<Item> initialState;
    private final Map<ImmutableSet<Item>, Map<Terminal, Action>> ACTION;
    private final Map<ImmutableSet<Item>, Map<NonTerminal, ImmutableSet<Item>>> GOTO;

    public ParsingTable(List<Production> augmentedGrammar) {
        var canonicalCollection = generateItems(augmentedGrammar);

        var firsts = generateFirsts(augmentedGrammar);
        var follows = generateFollows(augmentedGrammar, firsts);

        initialState = generateClosure(Set.of(new Item(augmentedGrammar.get(0), 0)), augmentedGrammar);
        ACTION = new HashMap<>();
        GOTO = new HashMap<>();

        Set<NonTerminal> nonTerminals = augmentedGrammar.stream()
                .flatMap(prod -> Stream.concat(Stream.of(prod.head), prod.getBody().stream()))
                .filter(sym -> sym.getClass().equals(NonTerminal.class))
                .map(sym -> (NonTerminal)sym)
                .collect(Collectors.toSet());
        Item acceptance = new Item(augmentedGrammar.get(0), 1);

        // First fill out GOTO for all NonTerminals for each state
        for (ImmutableSet<Item> state : canonicalCollection) {
            // Initialise transitions for current state
            var transitions = new HashMap<NonTerminal, ImmutableSet<Item>>();
            GOTO.put(state, transitions);

            for (NonTerminal nonTerminal : nonTerminals) {
                transitions.put(nonTerminal, generateGoto(state, nonTerminal, augmentedGrammar));
            }
        }

        for (ImmutableSet<Item> state : canonicalCollection) {
            // Initialise actions for current state
            var actions = new HashMap<Terminal, Action>();
            ACTION.put(state, actions);

            // ACCEPTANCES
            if (state.contains(acceptance)) {
                actions.put(Terminal.$, new Accept());
            }

            // REDUCTIONS
            // Items of type [ A -> alpha . ], where A is not the start symbol S'
            Set<Item> reductions = state.stream()
                    .filter(item -> (item.production.head != NonTerminal.START) && (item.production.size() == item.position))
                    .collect(Collectors.toSet());

            for (Item item : reductions) {
                Production prod = item.production;
                var reduce = new Reduce(prod);
                var follow = follows.get(prod.head);

                for (Terminal a : follow) {
                    if (actions.containsKey(a) && !actions.get(a).equals(reduce)) {
                        // conflicting actions, lang is not SLR(1)
                        throw new IllegalArgumentException("Conflicting actions were entered in the parsing table. Therefore, the specified grammar is not an LR(1) grammar.");
                    }

                    actions.put(a, reduce);
                }
            }

            // SHIFTS
            // Items of type [ A -> alpha . a beta ] where a is a terminal and not the last symbol
            HashSet<Item> shifts = state.stream()
                    .filter(item -> item.position < item.production.size() &&
                            item.production.getBody().get(item.position).getClass().equals(Terminal.class))
                    .collect(Collectors.toCollection(HashSet::new));

            for (Item item : shifts) {
                Symbol sym = item.production.getBody().get(item.position);

                if (sym.getClass().equals(Terminal.class)) {
                    Terminal a = (Terminal)sym;
                    var nextState = generateGoto(state, a, augmentedGrammar);
                    Shift shift = new Shift(nextState);

                    if (actions.containsKey(a) && !actions.get(a).equals(shift)) {
                        // conflicting actions, lang is not SLR(1)
                        throw new IllegalArgumentException("Conflicting actions were entered in the parsing table. Therefore, the specified grammar is not an LR(1) grammar.");
                    }

                    actions.put(a, shift);
                }
            }
        }

        // Any other entries are "error", which we can represent with no entry
    }

    /**
     * Lookup ACTION[s, a]
     *
     * @param s - the state on which the action is performed
     * @param a - the input terminal
     * @return ACTION[s, a] if it is defined; or null if it isn't
     */
    public Action ACTION(Set<Item> s, Terminal a) {
        var actionsOnState = ACTION.get(s);
        if (actionsOnState == null) return null;
        return actionsOnState.get(a);
    }

    /**
     * Lookup GOTO[s, A]
     *
     * @param s - the state from which we want to transition
     * @param A - the input non-terminal on the outgoing transition/edge
     *
     * @return GOTO[s, A] if it is defined; or null if it isn't
     */
    public ImmutableSet<Item> GOTO(Set<Item> s, NonTerminal A) {
        var gotoFromState = GOTO.get(s);
        if (gotoFromState == null) return null;
        return gotoFromState.get(A);
    }

    /**
     * Generate the closure of an item set for a grammar.
     *
     * @param I - an initial set of items
     * @param augmentedGrammar - a grammar augmented with a special start symbol production
     *
     * @return the closure of I
     */
    public static ImmutableSet<Item> generateClosure(Set<Item> I, List<Production> augmentedGrammar) {
        HashSet<Item> current;
        HashSet<Item> next = new HashSet<>(I);

        do {
            current = new HashSet<>(next);

            for (Item item : current) {
                // If dot is at right end, skip to next item
                if (item.position < item.production.getBody().size()) {
                    var symbol = item.production.getBody().get(item.position);
                    for (Production prod : augmentedGrammar) {
                        if (prod.head == symbol) {
                            next.add(new Item(prod, 0));
                        }
                    }
                }
            }
        } while (next.size() != current.size()); // until no more items are added to 'current' on one round

        return ImmutableSet.copyOf(current);
    }

    /**
     * Generate the closure of the set of items [A -> a X . b] such that
     * [A -> a . X b] is in I. Goto(I, X) represents the transition from
     * state I under input X.
     *
     * @param I - an initial set of items.
     * @param X - a grammar symbol.
     *
     * @return the closure of I
     */
    public static ImmutableSet<Item> generateGoto(Set<Item> I, Symbol X, List<Production> augmentedGrammar) {
        // Filter I for items for which X is immediately to the right of the dot
        // Then move the dot to after X
        var subset = I
                .stream()
                .filter(item -> (item.position < item.production.size())
                        && item.production.getBody().get(item.position) == X)
                .map(item -> new Item(item.production, item.position + 1))
                .collect(Collectors.toSet());

        // Then take closure of new set
        return generateClosure(subset, augmentedGrammar);
    }

    /**
     * Compute the canonical collection of sets of LR(0) items
     *
     * @param augmentedGrammar - a grammar augmented with a special start symbol production
     *
     * @return the canonical collection of sets of LR(0) items
     */
    public static Set<ImmutableSet<Item>> generateItems(List<Production> augmentedGrammar) {
        Set<Symbol> symbols = augmentedGrammar.stream()
                .flatMap(prod -> Stream.concat(Stream.of(prod.head), prod.getBody().stream()))
                .collect(Collectors.toSet()); // or NonTerminal + Terminal.values()
        var initialItem = new Item(augmentedGrammar.get(0), 0);
        var closure = generateClosure(Set.of(initialItem), augmentedGrammar);
        HashSet<ImmutableSet<Item>> current;
        HashSet<ImmutableSet<Item>> next = new HashSet<>(Set.of(closure));

        do {
            current = new HashSet<>(next);

            for (Set<Item> I : current) {
                for (Symbol X : symbols) {
                    var setToAdd = generateGoto(I,X,augmentedGrammar);
                    if (!setToAdd.isEmpty()) {
                        next.add(setToAdd);
                    }
                }
            }
        } while (current.size() != next.size()); // until no new items sets are added to C on one round

        return current;
    }

    /**
     * Compute FIRST(X), the set of terminal symbols that can begin a string derived
     * from X, for all symbols X in the grammar, critically assuming there are no
     * epsilon productions.
     *
     * @param augmentedGrammar - a grammar augmented with a special start symbol production,
     *                         with no epsilon productions.
     *
     * @return a map from X to FIRST(X) for all symbols X in the grammar.
     */
    public static Map<Symbol, Set<Terminal>> generateFirsts(List<Production> augmentedGrammar) {
        HashMap<Symbol, Set<Terminal>> firsts = new HashMap<>();

        Set<Terminal> terminals = augmentedGrammar.stream()
                .flatMap(prod -> prod.getBody().stream())
                .filter(sym -> sym.getClass().equals(Terminal.class))
                .map(sym -> (Terminal)sym)
                .collect(Collectors.toSet());

        for (Terminal X : terminals) {
            firsts.put(X, Set.of(X));
        }

        boolean updated;

        do {
            // As we only ever add terminals to firsts,
            // we can compare the size before and after each iteration to know when to terminate
            long sizeCurr = firsts.entrySet().stream()
                    .mapToLong(entry -> entry.getValue().size())
                    .sum();

            for (Production prod : augmentedGrammar) {
                var prev = firsts.getOrDefault(prod.head, new HashSet<>());
                prev.addAll(firsts.getOrDefault(prod.getBody().get(0), new HashSet<>()));
                firsts.put(prod.head, prev);
            }

            long sizeNext = firsts.entrySet().stream()
                    .mapToLong(entry -> entry.getValue().size())
                    .sum();

            updated = sizeCurr != sizeNext;
        } while (updated);

        return firsts;
    }

    /**
     *  Compute FOLLOW(A), the set of terminals a that can appear immediately to the
     *  right of a in sentential form, for all nonterminals A in the specified grammar.
     *
     * @param augmentedGrammar - a grammar augmented with a special start symbol production,
     *                         with no epsilon productions.
     * @param firsts - FIRST(X) for each symbol X in the grammar.
     *
     * @return a map from A to FOLLOW(A) for all nonterminals A in the grammar.
     */
    public static Map<NonTerminal, Set<Terminal>> generateFollows(List<Production> augmentedGrammar, Map<Symbol, Set<Terminal>> firsts) {
        var follows = new HashMap<NonTerminal, Set<Terminal>>();

        follows.put(NonTerminal.START, Set.of(Terminal.$));

        boolean updated;
        int count = 0;

        do {
            long sizeCurr = follows.entrySet().stream()
                    .mapToLong(entry -> entry.getValue().size())
                    .sum();

            for (Production prod : augmentedGrammar) {
                List<Symbol> body = prod.getBody();

                // for each B in production A -> alpha B beta
                for (int i = 0; i < body.size() - 1; i++) {
                    Symbol sym = body.get(i);

                    if (sym.getClass().equals(NonTerminal.class)) {
                        var B = (NonTerminal)sym;

                        var prev = follows.getOrDefault(B, new HashSet<>());
                        // Since we have assumed no epsilon productions,
                        // FIRST(beta) = FIRST(first symbol of beta)
                        prev.addAll(firsts.get(body.get(i + 1)));
                        follows.put(B, prev);
                    }
                    // Skip the symbol if it is not a NonTerminal
                }

                // for the last B, AKA for production A -> alpha B
                Symbol sym = body.get(body.size() - 1);
                if (sym.getClass().equals(NonTerminal.class)) {
                    var B = (NonTerminal)sym;

                    var prev = follows.getOrDefault(B, new HashSet<>());
                    prev.addAll(follows.getOrDefault(prod.head, new HashSet<>()));
                    follows.put(B, prev);
                }
            }

            long sizeNext = follows.entrySet().stream()
                    .mapToLong(entry -> entry.getValue().size())
                    .sum();

            updated = sizeCurr != sizeNext;
        } while (updated);

        return follows;
    }
}
