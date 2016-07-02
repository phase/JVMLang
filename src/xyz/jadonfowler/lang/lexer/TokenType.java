package xyz.jadonfowler.lang.lexer;

public enum TokenType {

    IDENTIFIER,
    INTEGER_LITERAL,
    STRING_LITERAL,
    /**
     * Generic symbol ( , . / ; ' [ ] ) etc
     */
    SYMBOL;

}
