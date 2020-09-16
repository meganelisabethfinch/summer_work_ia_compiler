package mef40;

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
}
