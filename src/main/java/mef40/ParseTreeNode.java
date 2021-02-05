package mef40;

import mef40.grammar.Symbol;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.List;

public class ParseTreeNode {
    private final Symbol symbol;
    private final List<ParseTreeNode> children;

    public ParseTreeNode(Symbol symbol, List<ParseTreeNode> children) {
        this.symbol = symbol;
        this.children = children == null ? Collections.emptyList() : children;
    }

    public ParseTreeNode(Symbol symbol, ParseTreeNode...children) {
        this.symbol = symbol;
        this.children = List.of(children);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || o.getClass() != this.getClass()) { return false; }
        ParseTreeNode node = (ParseTreeNode)o;
        return this.symbol.equals(node.symbol) && this.children.equals(node.children);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(symbol).append(children).toHashCode();
    }

    @Override
    public String toString() {
        String str = symbol.toString();

        for (ParseTreeNode node : children) {
            str = str + "(" + node.toString() + ")";
        }

        return str;
    }
}
