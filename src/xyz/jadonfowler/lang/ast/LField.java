package xyz.jadonfowler.lang.ast;

public class LField extends LVariable {

    private LClass parent;

    public LField(LClass parent, LType type, String name, Object value) {
        super(type, name, value);
        this.parent = parent;
    }

    public LClass getParent() {
        return parent;
    }

}
