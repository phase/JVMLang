package xyz.jadonfowler.lang.parser;

import java.util.ArrayList;
import java.util.List;

import xyz.jadonfowler.lang.ast.LConstructor;
import xyz.jadonfowler.lang.ast.LField;
import xyz.jadonfowler.lang.ast.LMethod;
import xyz.jadonfowler.lang.ast.LModifier;
import xyz.jadonfowler.lang.ast.LParameter;
import xyz.jadonfowler.lang.ast.LType;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;
import xyz.jadonfowler.lang.parser.Parser.Context;

public class ClassParser implements ContextParser {

    private Parser parser;

    public ClassParser(Parser parser) {
        this.parser = parser;
    }

    public void parse(Lexer lexer, Token token) {
        List<LModifier> modifiers = new ArrayList<LModifier>();
        if (LModifier.isModifier(token)) {
            modifiers.add(LModifier.getModifier(token));
            while (lexer.hasNext() && LModifier.isModifier((token = lexer.next())))
                modifiers.add(LModifier.getModifier(token));
        }
        lexer.pushBack();
        if (lexer.hasNext() && (token = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
            // public static... type
            LType type = LType.VOID;
            if (LType.isType(token.getValue())) {
                type = LType.getType(token.getValue());
            } else {
                lexer.pushBack();
            }
            if (lexer.hasNext() && (token = lexer.next()).getType().equals(TokenType.IDENTIFIER)) {
                // public... type name
                String name = token.getValue();

                if (lexer.hasNext() && (token = lexer.next()).getType().equals(TokenType.SYMBOL)) {
                    List<LParameter> parameters = new ArrayList<LParameter>();
                    // parse parameters
                    if (token.equals(Token.OPEN_PAREN)) {
                        while (lexer.hasNext()) {
                            token = lexer.next();
                            if (token.getType().equals(TokenType.SYMBOL)) {
                                if (!token.equals(Token.COMMA))
                                    break;
                            } else if (lexer.hasNext()) {
                                Token pname = lexer.next();
                                parameters.add(new LParameter(LType.getType(token.getValue()), pname.getValue()));
                            }
                        }
                    } else {
                        lexer.pushBack();
                    }
                    if (lexer.hasNext()) {
                        token = lexer.next();
                        if (token.getType().equals(TokenType.SYMBOL)) {
                            if (token.equals(Token.OPEN_BRACE)) {
                                if (name.equals("init")) {
                                    LConstructor constructor = new LConstructor(parser.getCurrentClass(), parameters, modifiers);
                                    parser.getCurrentClass().setConstructor(constructor);
                                    parser.setCurrentMethod(constructor);
                                } else {
                                    LMethod method = new LMethod(parser.getCurrentClass(), name, type, parameters, modifiers);
                                    parser.getCurrentClass().addMethod(method);
                                    parser.setCurrentMethod(method);
                                }
                                parser.setContext(Context.METHOD);
                            } else
                                lexer.pushBack();

                        }
                    }
                } else {
                    // public static int field
                    lexer.pushBack();
                    LField field = new LField(parser.getCurrentClass(), modifiers, type, name);
                    parser.getCurrentClass().addField(field);
                }
            }
        }
        if (token.equals(Token.CLOSE_BRACE)) {
            // done with class
            if (parser.getCurrentClass().getConstructor() == null) {
                LConstructor constructor = new LConstructor(parser.getCurrentClass());
                parser.getCurrentClass().setConstructor(constructor);
            }
            parser.setContext(Context.TOP);
        }
    }

}
