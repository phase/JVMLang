package xyz.jadonfowler.lang.lexer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer implements Iterator<Token> {

    private static HashMap<Pattern, TokenType> tokenPatterns;

    static {
        /*
         * Add Token Patterns
         */
        tokenPatterns = new HashMap<>();
        tokenPatterns.put(Pattern.compile("([a-zA-Z]+)"), TokenType.IDENTIFIER);
        tokenPatterns.put(Pattern.compile("((-)?[0-9]+)"), TokenType.INTEGER_LITERAL);
        tokenPatterns.put(Pattern.compile("(\".*\")"), TokenType.STRING_LITERAL);

        for (String token : new String[] {"=", "\\(", "\\)", "\\.", "\\,"}) {
            tokenPatterns.put(Pattern.compile("(" + token + ")"), TokenType.SYMBOL);
        }
    }

    private String source;
    private Token lastToken = null;
    private boolean pushBack = false;

    public Lexer(String program) {
        this.source = program;
    }

    @Override
    public boolean hasNext() {
        return !source.isEmpty();
    }

    @Override
    public Token next() {
        source = source.trim();

        if (pushBack) {
            pushBack = false;
            return lastToken;
        }

        for (Entry<Pattern, TokenType> pattern : tokenPatterns.entrySet()) {
            Matcher matcher = pattern.getKey().matcher(source);

            if (matcher.find()) {
                String token = matcher.group();
                source = matcher.replaceFirst("");

                if (pattern.getValue().equals(TokenType.STRING_LITERAL)) {
                    // remove quotes around string
                    return (lastToken = new Token(token.substring(1, token.length() - 1), TokenType.STRING_LITERAL));
                }

                return (lastToken = new Token(token, pattern.getValue()));
            }
        }

        return null;
    }

    public void pushBack() {
        if (lastToken != null) {
            pushBack = true;
        }
    }

    public String getSource() {
        return source;
    }

}
