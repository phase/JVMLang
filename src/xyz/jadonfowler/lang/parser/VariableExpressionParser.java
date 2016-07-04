package xyz.jadonfowler.lang.parser;

import xyz.jadonfowler.lang.ast.LVariable;
import xyz.jadonfowler.lang.ast.instruction.AssignLocalVariableToLiteralInstruction;
import xyz.jadonfowler.lang.ast.instruction.Instruction;
import xyz.jadonfowler.lang.lexer.Lexer;
import xyz.jadonfowler.lang.lexer.Token;
import xyz.jadonfowler.lang.lexer.TokenType;

public class VariableExpressionParser {

    private Parser parser;

    public VariableExpressionParser(Parser parser) {
        this.parser = parser;
    }

    private void addInstruction(Instruction i) {
        parser.getCurrentMethod().addInstruction(i);
    }

    public void parse(LVariable var, Lexer lexer) {
        Token cache;
        if (!lexer.hasNext())
            return;
        cache = lexer.next();
        switch (cache.getType()) {
            case INTEGER_LITERAL:
                String value = cache.getValue();
                if (lexer.hasNext()) {
                    cache = lexer.next();
                    if (cache.equals(Token.PERIOD)) {
                        if (!lexer.hasNext())
                            break;
                        cache = lexer.next();
                        if (cache.getType().equals(TokenType.INTEGER_LITERAL)) {
                            value += "." + cache.getValue();
                        } else {
                            lexer.pushBack();
                        }
                    } else {
                        lexer.pushBack();
                    }
                }
                try {
                    Integer i = Integer.parseInt(value);
                    addInstruction(new AssignLocalVariableToLiteralInstruction(var, i));
                } catch (NumberFormatException e) {
                    try {
                        Double d = Double.parseDouble(value);
                        addInstruction(new AssignLocalVariableToLiteralInstruction(var, d));
                    } catch (NumberFormatException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case STRING_LITERAL:
                addInstruction(new AssignLocalVariableToLiteralInstruction(var, cache.getValue()));
                break;
            case IDENTIFIER:
                break;
            case SYMBOL:
                break;
            default:
                break;
        }
    }

}
