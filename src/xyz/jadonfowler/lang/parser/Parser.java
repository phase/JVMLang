package xyz.jadonfowler.lang.parser;

import java.util.ArrayList;
import java.util.List;

import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LField;
import xyz.jadonfowler.lang.ast.LType;
import xyz.jadonfowler.lang.ast.Modifier;
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
                    if (Modifier.isModifier(token)) {
                        List<Modifier> modifiers = new ArrayList<Modifier>();
                        modifiers.add(Modifier.getModifier(token));
                        Token cache;
                        while (lexer.hasNext() && Modifier.isModifier((cache = lexer.next())))
                            modifiers.add(Modifier.getModifier(cache));
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
                                        if(cache.getValue().equals("(")) {
                                            
                                        } else {
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
