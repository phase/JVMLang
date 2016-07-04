package xyz.jadonfowler.lang.parser;

import java.util.ArrayList;
import java.util.List;

import xyz.jadonfowler.lang.ast.LClass;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;
import xyz.jadonfowler.lang.parser.Parser.Context;

public class TopParser implements ContextParser {

    private Parser parser;
    private String classModule;

    public TopParser(Parser parser) {
        this.parser = parser;
        this.classModule = "";
    }

    public void parse(Lexer lexer, Token token) {
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
                    parser.getClassTree().add(clazz);
                    parser.setCurrentClass(clazz);
                    parser.setContext(Context.CLASS);
                }
            }
        }
    }

}
