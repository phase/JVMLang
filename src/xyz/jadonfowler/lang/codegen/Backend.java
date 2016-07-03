package xyz.jadonfowler.lang.codegen;

import java.util.ArrayList;
import java.util.List;

import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;

public abstract class Backend {

    private AbstractClassTree act;

    public Backend(AbstractClassTree act) {
        this.act = act;
    }

    public byte[][] compileClasses() {
        List<byte[]> classes = new ArrayList<>();
        for (LClass clazz : act)
            classes.add(compile(clazz));
        return classes.toArray(new byte[classes.size()][]);
    }

    public byte[] compileClass(String className) {
        return compile(act.get(className));
    }

    public abstract byte[] compile(LClass clazz);

}
