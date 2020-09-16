package mef40.parser;

import com.google.common.collect.ImmutableSet;
import mef40.NonTerminal;
import mef40.Token;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class Reduce implements Action {
    public final Production production;


    public Reduce(Production production) { this.production = production; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Reduce reduce = (Reduce) o;
        return this.production.equals(reduce.production);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(production).toHashCode();
    }
}
