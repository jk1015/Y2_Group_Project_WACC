package wacc.instructions.expressions;

import wacc.instructions.Instruction;

public abstract class ExprInstruction implements Instruction {

    private final int register;

    public ExprInstruction(int register) {
        this.register = register;
    }

    public String getLocationString() {
        return "r" + register;
    }

}
