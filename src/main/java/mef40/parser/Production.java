package mef40.parser;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import mef40.NonTerminal;
import mef40.Symbol;

import java.util.List;
import java.util.stream.Collectors;

public class Production {
    public final NonTerminal head;
    private final List<Symbol> body;

    public Production(NonTerminal head, List<Symbol> body) {
        this.head = head;
        this.body = List.copyOf(body);
    }

    public Production(NonTerminal head, Symbol ...body) {
        this.head = head;
        this.body = List.of(body);
    }

    public int size() {
        return body.size();
    }

    public List<Symbol> getBody() {
        return List.copyOf(body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Production prod = (Production) o;
        return this.head.equals(prod.head) && this.body.equals(prod.body);
    }

    @Override
    public String toString() {
        return head.toString() + " -> " + body.stream().map(s -> s.toString()).collect(Collectors.joining(" "));
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(head).append(body).toHashCode();
    }


}
