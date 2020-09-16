package mef40.parser;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Shift implements Action {
    public final ImmutableSet<Item> state;

    Shift(ImmutableSet<Item> state) { this.state = state; }

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
