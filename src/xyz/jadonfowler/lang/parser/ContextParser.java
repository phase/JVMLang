package xyz.jadonfowler.lang.parser;

import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;

public interface ContextParser {

    void parse(Lexer lexer, Token token);

}
