package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LClass extends LType {

    private String module;
    private String name;
    private List<LField> fields;
    private List<LMethod> methods;

    public LClass(String module, String name) {
        this(module, name, new ArrayList<LField>(), new ArrayList<LMethod>());
    }

    public LClass(String module, String name, List<LField> fields, List<LMethod> methods) {
        super(name, "L" + module.replace(".", "/") + name + ";");
        this.module = module;
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }

    public String getModule() {
        return module;
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
