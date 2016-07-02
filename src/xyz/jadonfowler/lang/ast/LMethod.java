package xyz.jadonfowler.lang.ast;

public class LMethod {

    private LClass parent;
    private String name;
    private LType returnType;
    private LType[] arguments;

    public LMethod(LClass parent, String name, LType returnType, LType... arguments) {
        this.parent = parent;
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public LValue call(LValue... args) {
        // TODO ASM
        return new LValue(returnType, null);
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

    public LType[] getArguments() {
        return arguments;
    }

}