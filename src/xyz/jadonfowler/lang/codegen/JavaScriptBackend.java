package xyz.jadonfowler.lang.codegen;

import java.util.ArrayList;
import java.util.List;

import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LField;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.ast.LParameter;

public class JavaScriptBackend extends Backend {

    public JavaScriptBackend(AbstractClassTree act) {
        super(act);
    }

    @Override
    public byte[] compile(LClass clazz) {
        StringBuffer output = new StringBuffer();
        output.append("class " + clazz.getName() + " {\n\n");
        output.append("    constructor() {\n");
        for (LField field : clazz.getFields()) {
            output.append("        this." + field.getName() + " = " + field.getType().getDefaultValue() + ";\n");
        }
        output.append("    }\n\n");
        for (LMethod method : clazz.getMethods()) {
            List<String> parameters = new ArrayList<>();
            for (LParameter parameter : method.getParameters())
                parameters.add(parameter.getName());
            output.append("    " + method.getName() + " (" + String.join(", ", parameters) + ") {\n");
            output.append("    }\n\n");
        }
        output.append("}\n");
        return output.toString().getBytes();
    }

}
