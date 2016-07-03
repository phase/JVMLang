package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LClass extends LType {

    private String module;
    private String name;
    private List<LField> fields;
    private List<LMethod> methods;
    private LConstructor constructor;

    public LClass(String module, String name) {
        this(module, name, new ArrayList<LField>(), new ArrayList<LMethod>(), null);
    }

    public LClass(String module, String name, List<LField> fields, List<LMethod> methods, LConstructor constructor) {
        super(name, "L" + module.replace(".", "/") + name + ";");
        this.module = module;
        this.name = name;
        this.fields = fields;
        this.methods = methods;
        this.constructor = constructor;
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

    public LConstructor getConstructor() {
        return constructor;
    }

    public void setConstructor(LConstructor constructor) {
        if (this.constructor != null)
            throw new IllegalStateException("Class " + name + " already has a constructor!");
        this.constructor = constructor;
    }

}
