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
import xyz.jadonfowler.lang.ast.LVariable;
import xyz.jadonfowler.lang.ast.instruction.CreateLocalVariableInstruction;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;

public class Parser {

    private Lexer lexer;
    private Context context;
    private AbstractClassTree act;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.context = Context.TOP;
        this.act = new AbstractClassTree();
    }

    public void parse() {
        String classModule = "";
        LClass currentClass = null;
        LMethod currentMethod = null;
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
                                List<LParameter> parameters = new ArrayList<LParameter>();
                                // parse parameters
                                if (cache.getValue().equals("(")) {
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
                                } else {
                                    lexer.pushBack();
                                }
                                if (lexer.hasNext()) {
                                    cache = lexer.next();
                                    if (cache.getType().equals(TokenType.SYMBOL)) {
                                        if (cache.equals(Token.OPEN_BRACE)) {
                                            if (name.equals("init")) {
                                                LConstructor constructor = new LConstructor(currentClass, parameters, modifiers);
                                                currentClass.setConstructor(constructor);
                                                currentMethod = constructor;
                                            } else {
                                                LMethod method = new LMethod(currentClass, name, type, parameters, modifiers);
                                                currentClass.addMethod(method);
                                                currentMethod = method;
                                            }
                                            context = Context.IN_METHOD;
                                        } else
                                            lexer.pushBack();

                                    }
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
                        if (currentClass.getConstructor() == null) {
                            LConstructor constructor = new LConstructor(currentClass);
                            currentClass.setConstructor(constructor);
                        }
                        break lex;
                    }
                    break;
                case IN_METHOD:
                    if (token.getType().equals(TokenType.IDENTIFIER)) {
                        if (LType.isType(token.getValue())) {
                            LType type = LType.getType(token.getValue());
                            if (lexer.hasNext()) {
                                token = lexer.next();
                                switch (token.getType()) {
                                    case IDENTIFIER:
                                        // creating a variable:
                                        // "Type Identifier"
                                        String name = token.getValue();
                                        if (lexer.hasNext() && (token = lexer.next()).getType().equals(TokenType.SYMBOL)) {
                                            if (token.getValue().equals("=")) {
                                                currentMethod.addInstruction(new CreateLocalVariableInstruction(new LVariable(type, name, null)));
                                                // "Type ident ="
                                                // TODO parse expressions
                                            }
                                        } else if (token.getType().equals(TokenType.IDENTIFIER)) {
                                            // "Type ident ident" shouldn't
                                            // happen
                                            lexer.pushBack();
                                        }
                                        break;
                                    case INTEGER_LITERAL:
                                        break;
                                    case STRING_LITERAL:
                                        break;
                                    case SYMBOL:
                                        if (token.equals(Token.PERIOD)) {
                                            // accessing something from a type:
                                            // "Type."
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                // random type floating in method body
                                // allocate and return default value?
                            }
                        }
                    } else if (token.equals(Token.CLOSE_BRACE)) {
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
