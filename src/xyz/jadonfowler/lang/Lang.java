package xyz.jadonfowler.lang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LField;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.ast.instruction.Instruction;
import xyz.jadonfowler.lang.codegen.JVMBackend;
import xyz.jadonfowler.lang.codegen.JavaScriptBackend;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.parser.Parser;

public class Lang {

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            if (file.isDirectory()) {
                // TODO recursive file parsing
            } else if (file.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(Paths.get(file.getPath())), Charset.defaultCharset());
                    Lexer lexer = new Lexer(content);
                    Parser parser = new Parser(lexer);
                    parser.parse();
                    for (LClass clazz : parser.getClassTree()) {
                        System.out.println("Class: " + clazz.getName());
                        System.out.println("  Module: " + clazz.getModule());
                        System.out.println("  Constructor: " + clazz.getConstructor().toString());
                        for (LField field : clazz.getFields()) {
                            System.out.println("  Field: " + field.getModifiers().toString() + " " + field.getType() + " " + field.getName());
                        }
                        for (LMethod method : clazz.getMethods()) {
                            System.out
                                    .println("  Method: " + method.toString());
                            for (Instruction ins : method.getInstructions()) {
                                System.out.println("    " + ins);
                            }
                        }
                    }
                    JavaScriptBackend js = new JavaScriptBackend(parser.getClassTree());
                    JVMBackend jvm = new JVMBackend(parser.getClassTree());
                    for (LClass clazz : parser.getClassTree()) {
                        String name = clazz.getName();
                        { // javascript
                            byte[] jsClass = js.compileClass(name);
                            File jsFile = new File("output/js/src/" + name + ".js");
                            jsFile.mkdirs();
                            jsFile.delete();
                            jsFile.createNewFile();
                            try (FileOutputStream stream = new FileOutputStream(jsFile)) {
                                stream.write(jsClass);
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        { // jvm
                            byte[] jvmClass = jvm.compileClass(name);
                            File classFile = new File("output/jvm/src/" + clazz.getModule().replace(".", "/") + "/" + name + ".class");
                            classFile.mkdirs();
                            classFile.delete();
                            classFile.createNewFile();
                            try (FileOutputStream stream = new FileOutputStream(classFile)) {
                                stream.write(jvmClass);
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (!file.exists()) {
                System.err.println(file + " does not exist!");
            }
        }
    }

}
