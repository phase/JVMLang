package xyz.jadonfowler.lang.ast.instruction;

import xyz.jadonfowler.lang.ast.LVariable;

public class CreateLocalVariableInstruction extends Instruction {

    private LVariable variable;

    public CreateLocalVariableInstruction(LVariable variable) {
        this.variable = variable;
    }
    
    public LVariable getVariable() {
        return variable;
    }

    @Override
    public String toString() {
        return "CreateLocalVariableInstruction(" + variable + ")";
    }
    
}
