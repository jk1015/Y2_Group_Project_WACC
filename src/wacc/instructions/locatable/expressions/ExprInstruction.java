package wacc.instructions.locatable.expressions;

import wacc.instructions.locatable.LocatableInstruction;
import wacc.types.Type;

public abstract class ExprInstruction implements LocatableInstruction {


    private int register;
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

    public void setRegister(int i) {
        this.register = i;
    }


}
