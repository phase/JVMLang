package xyz.jadonfowler.lang.parser;

import java.util.ArrayList;
import java.util.List;

import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LField;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.ast.LModifier;
import xyz.jadonfowler.lang.ast.LParameter;
import xyz.jadonfowler.lang.ast.LType;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;

public class Parser {

    // static tokens
    public static final Token CLASS_TOKEN = new Token("class", TokenType.IDENTIFIER);
    public static final Token OPEN_BRACE_TOKEN = new Token("{", TokenType.SYMBOL);
    public static final Token CLOSE_BRACE_TOKEN = new Token("}", TokenType.SYMBOL);

    private Lexer lexer;
    private Context context;
    private AbstractClassTree act;
    private LClass currentClass;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.context = Context.TOP;
        this.act = new AbstractClassTree();
        this.currentClass = null;
    }

    public void parse() {
        lex: for (Token token : lexer) {
            System.out.println(context + ": " + token + " ");
            switch (context) {
                case TOP:
                    if (token.equals(CLASS_TOKEN)) {
                        String className;
                        Token cache;
                        if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                            className = cache.getValue();
                            if (lexer.hasNext() && (cache = lexer.next()).equals(OPEN_BRACE_TOKEN)) {
                                LClass clazz = new LClass(className);
                                act.add(clazz);
                                currentClass = clazz;
                                context = Context.IN_CLASS;
                            }
                        }
                    }
                    break;
                case IN_CLASS:
                    if (LModifier.isModifier(token)) {
                        List<LModifier> modifiers = new ArrayList<LModifier>();
                        modifiers.add(LModifier.getModifier(token));
                        Token cache;
                        while (lexer.hasNext() && LModifier.isModifier((cache = lexer.next())))
                            modifiers.add(LModifier.getModifier(cache));
                        lexer.pushBack();
                        if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                            // public static... type
                            if (LType.isType(cache.getValue())) {
                                LType type = LType.getType(cache.getValue());
                                if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                                    // public... type name
                                    String name = cache.getValue();

                                    if (lexer.hasNext() && (cache = lexer.next()).getType().equals(TokenType.SYMBOL)) {
                                        // public static void method (
                                        if (cache.getValue().equals("(")) {
                                            List<LParameter> parameters = new ArrayList<LParameter>();
                                            while (lexer.hasNext()) {
                                                Token ptype = lexer.next();
                                                Token pname = lexer.next();
                                                parameters.add(new LParameter(LType.getType(ptype.getValue()), pname.getValue()));
                                                cache = lexer.next();
                                                if (cache.getType().equals(TokenType.SYMBOL)) {
                                                    if (cache.getValue().equals(","))
                                                        continue;
                                                    if (cache.getValue().equals(")"))
                                                        break;
                                                } else {
                                                    lexer.pushBack();
                                                    break;
                                                }
                                            }
                                            if (lexer.hasNext()) {
                                                cache = lexer.next();
                                                if (cache.getType().equals(TokenType.SYMBOL)) {
                                                    if (cache.getValue().equals("{")) {
                                                        LMethod method = new LMethod(currentClass, name, type, parameters, modifiers);
                                                        currentClass.addMethod(method);
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
                        } else {
                            // random modifiers without an identifier after
                            // them...
                        }
                    }
                    if (token.equals(CLOSE_BRACE_TOKEN)) {
                        // done with class
                        break lex;
                    }
                    break;
                case IN_METHOD:
                    if (token.equals(CLOSE_BRACE_TOKEN)) {
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
