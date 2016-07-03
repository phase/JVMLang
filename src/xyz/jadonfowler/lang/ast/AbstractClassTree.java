package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbstractClassTree implements Iterable<LClass> {

    private List<LClass> classes;

    public AbstractClassTree() {
        this.classes = new ArrayList<LClass>();
    }

    public AbstractClassTree(AbstractClassTree act) {
        this.classes = act.classes;
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

    @Override
    public Iterator<LClass> iterator() {
        return new ClassIterator(classes);
    }

}
