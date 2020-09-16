package mef40;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UFloat extends Token {
    public final float value;

    public UFloat(float v) {
        super(Terminal.UFLOAT);
        value = v;
    }

    @Override
    public String toString() {
        return "<" + tag + ", " + value + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        UFloat ufloat = (UFloat)o;
        return this.value == ufloat.value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(value).toHashCode();
    }
}
