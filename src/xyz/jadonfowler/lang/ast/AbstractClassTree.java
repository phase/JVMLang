package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class AbstractClassTree {

    private List<LClass> classes;

    public AbstractClassTree() {
        this.classes = new ArrayList<LClass>();
    }

    public List<LClass> getClasses() {
        return classes;
    }

    public LClass get(String name) {
        for (LClass clazz : classes) {
            if (clazz.getName().equals(name))
                return clazz;
        }
        return null;
    }

    public void add(LClass clazz) {
        classes.add(clazz);
    }

}
