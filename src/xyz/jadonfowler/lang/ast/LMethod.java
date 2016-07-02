package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LMethod extends Scope {

    private LClass parent;
    private String name;
    private LType returnType;
    private List<LParameter> parameters;
    private List<LModifier> modifiers;

    public LMethod(LClass parent, String name, LType returnType) {
        this(parent, name, returnType, new ArrayList<>(), new ArrayList<>());
    }

    public LMethod(LClass parent, String name, LType returnType, List<LParameter> parameters, List<LModifier> modifiers) {
        this.parent = parent;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.modifiers = modifiers;
    }

    public LClass getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public LType getReturnType() {
        return returnType;
    }

    public List<LParameter> getParameters() {
        return parameters;
    }

    public List<LModifier> getModifiers() {
        return modifiers;
    }

}
