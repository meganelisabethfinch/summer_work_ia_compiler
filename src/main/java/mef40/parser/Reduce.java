package mef40.parser;

import com.google.common.collect.ImmutableSet;
import mef40.NonTerminal;
import mef40.Token;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class Reduce implements Action {
    public final Production production;

    public final Map<ImmutableSet<Item>, Map<NonTerminal, ImmutableSet<Item>>> GOTO; // Maybe switch to ImmutableMap to be safe?

    public Reduce(Production production, Map<ImmutableSet<Item>, Map<NonTerminal, ImmutableSet<Item>>> GOTO) {
        this.production = production;
        // A reference to the GOTO part of parsing table - do NOT take a copy.
        this.GOTO = GOTO;
    }

    @Override
    public void execute(Stack<ImmutableSet<Item>> states, Queue<Token> tokens) {
        System.out.println(production);

        for (int i = 0; i < production.size(); i++) {
            states.pop();
        }

        ImmutableSet<Item> s = GOTO.get(states.peek()).get(production.head);
        states.push(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Reduce reduce = (Reduce) o;
        return (this.production.equals(reduce.production)) && (this.GOTO.equals(reduce.GOTO));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(production).append(GOTO).toHashCode();
    }
}
