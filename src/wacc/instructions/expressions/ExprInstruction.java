package wacc.instructions.expressions;

import wacc.instructions.LocatableInstruction;
import wacc.types.Type;

public abstract class ExprInstruction implements LocatableInstruction {

    private final int register;
    private final Type type;

    public ExprInstruction(int register, Type type) {
        this.register = register;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getLocationString() {
        return "r" + register;
    }

}
