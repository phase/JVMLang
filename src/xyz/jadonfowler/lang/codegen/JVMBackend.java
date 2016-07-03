package xyz.jadonfowler.lang.codegen;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LField;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.ast.LModifier;
import xyz.jadonfowler.lang.ast.LParameter;
import xyz.jadonfowler.lang.ast.LType;

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
            cw.visit(V1_8, ACC_PUBLIC, clazz.getModule() + "." + clazz.getName(), null, "java/lang/Object", null);
            // constructor
            StringBuffer constructorDescription = new StringBuffer();
            constructorDescription.append("(");
            for (LParameter parameter : clazz.getConstructor().getParameters())
                constructorDescription.append(parameter.getType().getDescription());
            constructorDescription.append(")V"); // constructors return void
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", constructorDescription.toString(), null, null);
            mv.visitCode();
            // mv.visitVarInsn(ALOAD, 0);
            // mv.visitMethodInsn(0, "java/lang/Object", "<init>",
            // constructorDescription.toString(), false);
            // fields are initialized in the constructor
            for (LField field : clazz.getFields()) {
                if (field.getType().equals(LType.VOID))
                    continue; // ignore void variables
                fv = cw.visitField(0, field.getName(), field.getType().getDescription(), null, null);
                fv.visitEnd();
            }
            // mv.visitMaxs(1 + fieldCount, 1 + this.variables.size() +
            // argumentCount);
            mv.visitInsn(RETURN);
            mv.visitEnd();

            // methods
            for (LMethod method : clazz.getMethods()) {
                int modifiers = 0;
                for (LModifier modifier : method.getModifiers())
                    modifiers |= modifier.getModifier();
                StringBuffer methodDescription = new StringBuffer();
                methodDescription.append("(");
                for (LParameter parameter : method.getParameters())
                    methodDescription.append(parameter.getType().getDescription());
                methodDescription.append(")" + method.getReturnType().getDescription());
                mv = cw.visitMethod(modifiers, method.getName(), methodDescription.toString(), null, null);
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
