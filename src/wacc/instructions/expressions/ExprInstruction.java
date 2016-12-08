package wacc.instructions.expressions;

import wacc.instructions.LocatableInstruction;
import wacc.types.Type;

public abstract class ExprInstruction implements LocatableInstruction {


    private int register;
    private Type type;

    public ExprInstruction(int register, Type type) {
        this.register = register;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean usesRegister() {
        return true;
    }

    public String getLocationString() {
        return "r" + register;
    }

    public void setRegister(int i) {
        this.register = i;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
