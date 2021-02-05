package mef40;

import mef40.grammar.Terminal;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Token {
    public final Terminal tag;
    public Token(Terminal t) { tag = t; }

    @Override
    public String toString() {
        return "<" + tag + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Token token = (Token)o;
        return (this.tag == token.tag);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(tag).toHashCode();
    }
}
