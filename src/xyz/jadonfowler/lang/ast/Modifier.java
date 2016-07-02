package xyz.jadonfowler.lang.ast;

import org.objectweb.asm.Opcodes;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;

public class Modifier implements Opcodes {

    public static Modifier PUBLIC = new Modifier(new Token("public", TokenType.IDENTIFIER), ACC_PUBLIC);
    public static Modifier PRIVATE = new Modifier(new Token("private", TokenType.IDENTIFIER), ACC_PRIVATE);
    public static Modifier PROTECTED = new Modifier(new Token("protected", TokenType.IDENTIFIER), ACC_PROTECTED);
    public static Modifier STATIC = new Modifier(new Token("static", TokenType.IDENTIFIER), ACC_STATIC);

    private Token token;
    private int modifier;

    private Modifier(Token token, int modifier) {
        this.token = token;
        this.modifier = modifier;
    }

    public static boolean isModifier(Token token) {
        // TODO Find a better way to do this (Probably a HashMap)
        if (token.equals(PUBLIC.getToken()))
            return true;
        if (token.equals(PRIVATE.getToken()))
            return true;
        if (token.equals(PROTECTED.getToken()))
            return true;
        if (token.equals(STATIC.getToken()))
            return true;
        return false;
    }

    public static Modifier getModifier(Token token) {
        // TODO find a better way to do this
        if (token.getValue().equals(PUBLIC.getToken().getValue()))
            return PUBLIC;
        if (token.getValue().equals(PRIVATE.getToken().getValue()))
            return PRIVATE;
        if (token.getValue().equals(PROTECTED.getToken().getValue()))
            return PROTECTED;
        if (token.getValue().equals(STATIC.getToken().getValue()))
            return STATIC;
        return null;
    }

    @Override
    public String toString() {
        return token.getValue();
    }

    public Token getToken() {
        return token;
    }

    public int getModifier() {
        return modifier;
    }

}
