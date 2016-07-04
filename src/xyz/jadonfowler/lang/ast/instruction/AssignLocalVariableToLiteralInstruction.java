package xyz.jadonfowler.lang.ast.instruction;

import xyz.jadonfowler.lang.ast.LVariable;

public class AssignLocalVariableToLiteralInstruction extends Instruction {

    private LVariable variable;
    private Object value;

    public AssignLocalVariableToLiteralInstruction(LVariable variable, Object value) {
        variable.setValue(value);
        this.variable = variable;
        this.value = value;
    }

    public LVariable getVariable() {
        return variable;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AssignLocalVariableToLiteralInstruction(" + variable + " " + value + " (" + value.getClass().getName() + ") )";
    }

}
