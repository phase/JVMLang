package xyz.jadonfowler.lang.ast;

public class LValue {

    private LType type;
    private Object value;

    public LValue(LType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public LType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

}
