package xyz.jadonfowler.lang.ast;

public class LParameter {

    private LType type;
    private String name;

    public LParameter(LType type, String name) {
        this.type = type;
        this.name = name;
    }

    public LType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

}
