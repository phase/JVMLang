package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LMethod extends Scope {

    private LClass parent;
    private String name;
    private LType returnType;
    private LType[] parameters;
    private List<Modifier> modifiers;

    public LMethod(LClass parent, String name, LType returnType, LType... parameters) {
        this.parent = parent;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.modifiers = new ArrayList<>();
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

    public LType[] getParameters() {
        return parameters;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

}
