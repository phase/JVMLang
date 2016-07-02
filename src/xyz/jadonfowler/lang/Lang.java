package xyz.jadonfowler.lang;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
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
                    for (Token token : new Lexer(lexer))
                        System.out.println(token);
                    Parser parser = new Parser(lexer);
                    parser.parse();
                    for (LClass clazz : parser.getClassTree().getClasses()) {
                        System.out.println(clazz.getName());
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
