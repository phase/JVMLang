package xyz.jadonfowler.lang.parser;

import xyz.jadonfowler.lang.ast.AbstractClassTree;
import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;

public class Parser {

    private Lexer lexer;
    private Context context;
    private AbstractClassTree act;

    private TopParser topParser;
    private ClassParser classParser;
    private MethodParser methodParser;

    private LClass currentClass = null;
    private LMethod currentMethod = null;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.context = Context.TOP;
        this.act = new AbstractClassTree();
        this.topParser = new TopParser(this);
        this.classParser = new ClassParser(this);
        this.methodParser = new MethodParser(this);
    }

    public void parse() {
        for (Token token : lexer) {
            switch (context) {
                case TOP:
                    topParser.parse(lexer, token);
                    break;
                case CLASS:
                    classParser.parse(lexer, token);
                    break;
                case METHOD:
                    methodParser.parse(lexer, token);
                    break;
            }
        }

    }

    protected Context getContext() {
        return context;
    }

    protected void setContext(Context c) {
        context = c;
    }

    protected LClass getCurrentClass() {
        return currentClass;
    }

    protected void setCurrentClass(LClass clazz) {
        currentClass = clazz;
    }

    protected LMethod getCurrentMethod() {
        return currentMethod;
    }

    protected void setCurrentMethod(LMethod method) {
        currentMethod = method;
    }

    protected Lexer getLexer() {
        return lexer;
    }

    public AbstractClassTree getClassTree() {
        return act;
    }

    enum Context {
        TOP,
        CLASS,
        METHOD;
    }

}
