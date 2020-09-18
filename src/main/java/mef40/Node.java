package mef40;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.List;

public class Node {
    private final Symbol symbol;
    private final List<Node> children;

    public Node(Symbol symbol, List<Node> children) {
        this.symbol = symbol;
        this.children = children == null ? Collections.emptyList() : children;
    }

    public Node(Symbol symbol, Node ...children) {
        this.symbol = symbol;
        this.children = List.of(children);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || o.getClass() != this.getClass()) { return false; }
        Node node = (Node)o;
        return this.symbol.equals(node.symbol) && this.children.equals(node.children);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(symbol).append(children).toHashCode();
    }

    @Override
    public String toString() {
        String str = symbol.toString();

        for (Node node : children) {
            str = str + "(" + node.toString() + ")";
        }

        return str;
    }
}
