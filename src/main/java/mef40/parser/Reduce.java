package mef40.parser;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
