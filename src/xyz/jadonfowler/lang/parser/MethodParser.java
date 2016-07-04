package xyz.jadonfowler.lang.parser;

import xyz.jadonfowler.lang.ast.LType;
import xyz.jadonfowler.lang.ast.LVariable;
import xyz.jadonfowler.lang.ast.instruction.CreateLocalVariableInstruction;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;
import xyz.jadonfowler.lang.parser.Parser.Context;

public class MethodParser implements ContextParser {

    private Parser parser;

    public MethodParser(Parser parser) {
        this.parser = parser;
    }

    public void parse(Lexer lexer, Token token) {
        if (token.getType().equals(TokenType.IDENTIFIER)) {
            if (LType.isType(token.getValue())) {
                LType type = LType.getType(token.getValue());
                if (lexer.hasNext()) {
                    token = lexer.next();
                    switch (token.getType()) {
                        case IDENTIFIER:
                            // creating a variable:
                            // "Type Identifier"
                            String name = token.getValue();
                            if (lexer.hasNext() && (token = lexer.next()).getType().equals(TokenType.SYMBOL)) {
                                if (token.getValue().equals("=")) {
                                    parser.getCurrentMethod().addInstruction(new CreateLocalVariableInstruction(new LVariable(type, name, null)));
                                    // "Type ident ="
                                    // TODO parse expressions
                                }
                            } else if (token.getType().equals(TokenType.IDENTIFIER)) {
                                // "Type ident ident" shouldn't
                                // happen
                                lexer.pushBack();
                            }
                            break;
                        case INTEGER_LITERAL:
                            break;
                        case STRING_LITERAL:
                            break;
                        case SYMBOL:
                            if (token.equals(Token.PERIOD)) {
                                // accessing something from a type:
                                // "Type."
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    // random type floating in method body
                    // allocate and return default value?
                }
            }
        } else if (token.equals(Token.CLOSE_BRACE)) {
            // done with method
            parser.setContext(Context.CLASS);
        }
    }

}
