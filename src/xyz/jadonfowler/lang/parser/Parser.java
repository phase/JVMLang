package xyz.jadonfowler.lang.parser;

import java.util.ArrayList;
import java.util.List;

import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LConstructor;
import xyz.jadonfowler.lang.ast.LField;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.ast.LModifier;
import xyz.jadonfowler.lang.ast.LParameter;
import xyz.jadonfowler.lang.ast.LType;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;

public class Parser {

    private Lexer lexer;
    private Context context;
    private AbstractClassTree act;
    private String classModule;
    private LClass currentClass;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.context = Context.TOP;
        this.act = new AbstractClassTree();
        this.classModule = "";
        this.currentClass = null;
    }

    public void parse() {
        lex: for (Token token : lexer) {
            // System.out.println(context + ": " + token + " ");
            switch (context) {
                case TOP:
                    if (token.equals(Token.MODULE)) {
                        // package a.b.c.d class Blah
                        Token cache;
                        List<String> modulePath = new ArrayList<>();
                        while (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                            modulePath.add(cache.getValue());
                            if (!lexer.next().equals(Token.PERIOD))
                                break;
                        }
                        lexer.pushBack();
                        classModule = String.join(".", modulePath);
                    } else if (token.equals(Token.CLASS)) {
                        String className;
                        Token cache;
                        if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                            className = cache.getValue();
                            if (lexer.hasNext() && (cache = lexer.next()).equals(Token.OPEN_BRACE)) {
                                LClass clazz = new LClass(classModule, className);
                                act.add(clazz);
                                currentClass = clazz;
                                context = Context.IN_CLASS;
                            }
                        }
                    }
                    break;
                case IN_CLASS:
                    List<LModifier> modifiers = new ArrayList<LModifier>();
                    Token cache;
                    if (LModifier.isModifier(token)) {
                        modifiers.add(LModifier.getModifier(token));
                        while (lexer.hasNext() && LModifier.isModifier((cache = lexer.next())))
                            modifiers.add(LModifier.getModifier(cache));
                    }
                    lexer.pushBack();
                    if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                        // public static... type
                        LType type = LType.VOID;
                        if (LType.isType(cache.getValue())) {
                            type = LType.getType(cache.getValue());
                        } else {
                            lexer.pushBack();
                        }
                        if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                            // public... type name
                            String name = cache.getValue();

                            if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.SYMBOL)) {
                                // public static void method (
                                if (cache.getValue().equals("(")) {
                                    List<LParameter> parameters = new ArrayList<LParameter>();
                                    while (lexer.hasNext()) {
                                        cache = lexer.next();
                                        if (cache.getType().equals(TokenType.SYMBOL)) {
                                            if (!cache.equals(Token.COMMA))
                                                break;
                                        } else if (lexer.hasNext()) {
                                            Token pname = lexer.next();
                                            parameters.add(new LParameter(LType.getType(cache.getValue()), pname.getValue()));
                                        }
                                    }
                                    if (lexer.hasNext()) {
                                        cache = lexer.next();
                                        if (cache.getType().equals(TokenType.SYMBOL)) {
                                            if (cache.equals(Token.OPEN_BRACE)) {
                                                if (name.equals("init")) {
                                                    LConstructor constructor = new LConstructor(currentClass, name, parameters, modifiers);
                                                    currentClass.setConstructor(constructor);
                                                } else {
                                                    LMethod method = new LMethod(currentClass, name, type, parameters, modifiers);
                                                    currentClass.addMethod(method);
                                                }
                                                context = Context.IN_METHOD;
                                            } else
                                                lexer.pushBack();

                                        }
                                    }
                                } else {
                                    // The symbol must be for something
                                    // else, so we're going to ignore it
                                    // and parse what we have as a
                                    // field.
                                    lexer.pushBack();
                                    LField field = new LField(currentClass, modifiers, type, name);
                                    currentClass.addField(field);
                                }
                            } else {
                                // public static int field
                                lexer.pushBack();
                                LField field = new LField(currentClass, modifiers, type, name);
                                currentClass.addField(field);
                            }
                        }
                    }
                    if (token.equals(Token.CLOSE_BRACE)) {
                        // done with class
                        break lex;
                    }
                    break;
                case IN_METHOD:
                    if (token.equals(Token.CLOSE_BRACE)) {
                        // done with method
                        context = Context.IN_CLASS;
                    }
                    break;
            }
        }

    }

    public AbstractClassTree getClassTree() {
        return act;
    }

    enum Context {
        TOP,
        IN_CLASS,
        IN_METHOD;
    }

}
