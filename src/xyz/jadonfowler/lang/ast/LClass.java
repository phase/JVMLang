package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LClass extends LType {

    private String name;
    private List<LField> fields;
    private List<LMethod> methods;

    public LClass(String name) {
        this(name, new ArrayList<LField>(), new ArrayList<LMethod>());
    }

    public LClass(String name, List<LField> fields, List<LMethod> methods) {
        // TODO packages
        super(name, "Llang/" + name + ";");
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public List<LField> getFields() {
        return fields;
    }

    public void addField(LField f) {
        fields.add(f);
    }

    public List<LMethod> getMethods() {
        return methods;
    }

    public void addMethod(LMethod m) {
        methods.add(m);
    }

}
