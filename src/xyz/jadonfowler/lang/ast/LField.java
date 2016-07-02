package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LField extends LVariable {

    private LClass parent;
    private List<LModifier> modifiers;

    public LField(LClass parent, LType type, String name) {
        this(parent, new ArrayList<>(), type, name);
    }

    public LField(LClass parent, List<LModifier> modifiers, LType type, String name) {
        this(parent, modifiers, type, name, null);
    }

    public LField(LClass parent, List<LModifier> modifiers, LType type, String name, Object value) {
        super(type, name, value);
        this.parent = parent;
        this.modifiers = modifiers;
    }

    public LClass getParent() {
        return parent;
    }

    public List<LModifier> getModifiers() {
        return modifiers;
    }

}
