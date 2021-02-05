package mef40.parser;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Item {
    public final Production production;
    public final int position;

    public Item(Production prod, int pos) {
        if (pos > prod.size()) {
            throw new IllegalArgumentException("Position cannot exceed production size by more than one.");
        }
        this.production = prod;
        this.position = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Item item = (Item)o;
        return (this.position == item.position) && this.production.equals(item.production);
    }

    @Override
    public String toString() {
        var prod = new ArrayList<>(Arrays.asList(production.toString().split(" ")));
        prod.add(position + 2, ".");
        return prod.stream().collect(Collectors.joining(" "));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(production).append(position).toHashCode();
    }
}
