package xyz.jadonfowler.lang.parser;

import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
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
                        Token t;
                        if (lexer.hasNext() && (t = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                            className = t.getValue();
                            if (lexer.hasNext() && (t = lexer.next()).equals(OPEN_BRACE_TOKEN)) {
                                LClass clazz = new LClass(className);
                                act.add(clazz);
                                currentClass = clazz;
                                context = Context.IN_CLASS;
                            }
                        }
                    }
                    break;
                case IN_CLASS:
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
