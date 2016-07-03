package xyz.jadonfowler.lang.ast;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class ClassIterator implements Iterator<LClass> {

    private Stack<LClass> classes;

    public ClassIterator(Collection<LClass> classes) {
        this.classes = new Stack<>();
        this.classes.addAll(classes);
    }

    @Override
    public boolean hasNext() {
        return classes.size() > 0;
    }

    @Override
    public LClass next() {
        return classes.pop();
    }

}
