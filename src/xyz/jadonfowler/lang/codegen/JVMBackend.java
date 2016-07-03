package xyz.jadonfowler.lang.codegen;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.ast.LModifier;
import xyz.jadonfowler.lang.ast.LParameter;

public class JVMBackend extends Backend implements Opcodes {

    public JVMBackend(AbstractClassTree act) {
        super(act);
    }

    @Override
    public byte[] compile(LClass clazz) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        FieldVisitor fv;
        MethodVisitor mv;
        {
            // TODO packages
            cw.visit(V1_8, ACC_PUBLIC, clazz.getModule() + clazz.getName(), null, "java/lang/Object", null);
            for (LMethod method : clazz.getMethods()) {
                int modifiers = 0;
                for (LModifier modifier : method.getModifiers())
                    modifiers |= modifier.getModifier();
                StringBuffer description = new StringBuffer();
                description.append("(");
                for (LParameter parameter : method.getParameters())
                    description.append(parameter.getType().getDescription());
                description.append(")" + method.getReturnType().getDescription());
                mv = cw.visitMethod(modifiers, method.getName(), description.toString(), null, null);
                mv.visitCode();
                // end
                mv.visitInsn(RETURN);
                // mv.visitMaxs(variableCount > 0 ? 1 : 0, variableCount +
                // argumentCount);
                mv.visitEnd();
            }
            cw.visitEnd();
        }

        return cw.toByteArray();
    }

}
