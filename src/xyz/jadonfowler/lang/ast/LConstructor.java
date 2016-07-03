package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LConstructor extends LMethod {

    public LConstructor(LClass parent) {
        this(parent, new ArrayList<>(), new ArrayList<>());
    }

    public LConstructor(LClass parent, List<LParameter> parameters, List<LModifier> modifiers) {
        super(parent, "init", parent, parameters, modifiers);
        // constructors can only be public
        this.modifiers = new ArrayList<LModifier>();
        this.modifiers.add(LModifier.PUBLIC);
    }

}
