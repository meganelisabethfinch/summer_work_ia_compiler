package mef40.parser;

import mef40.Token;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Shift implements Action {
    public final Set<Item> state; // Maybe switch to ImmutableSet to be safe?

    Shift(Set<Item> state) { this.state = state; }

    @Override
    public void execute(Stack<Set<Item>> stack, Queue<Token> tokens) {
        stack.push(state);
        tokens.poll();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Shift shift = (Shift) o;
        return (this.state.equals(shift.state));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(state).toHashCode();
    }
}
