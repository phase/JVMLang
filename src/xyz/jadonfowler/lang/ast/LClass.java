package xyz.jadonfowler.lang.ast;

public class LClass extends Scope {

    private String name;
    private LField[] fields;
    private LMethod[] methods;

    public LClass(String name, LField[] fields, LMethod[] methods) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }

    public LClass(String name) {
        this(name, null, null);
    }

    public String getName() {
        return name;
    }

    public LField[] getFields() {
        return fields;
    }

    public LMethod[] getMethods() {
        return methods;
    }

}
