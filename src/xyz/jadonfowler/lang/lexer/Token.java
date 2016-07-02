package xyz.jadonfowler.lang.lexer;

public class Token {

    private String value;
    private TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString() + "(\"" + value + "\")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Token) {
            Token t = (Token) o;
            if (t.getValue().equals(this.value) && t.getType() == this.type) {
                return true;
            }
        }
        return false;
    }

    public static boolean tokensAreEqual(Token[] a, Token[] b) {
        if (a.length == b.length) {
            int length = a.length;
            for (int i = 0; i < length; i++)
                if (!a[i].equals(b[i]))
                    return false;
            return true;
        }
        return false;
    }

}
