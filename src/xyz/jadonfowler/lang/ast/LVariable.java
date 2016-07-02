package xyz.jadonfowler.lang.ast;

public class LVariable extends LValue {

    private String name;

    public LVariable(LType type, String name, Object value) {
        super(type, value);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
