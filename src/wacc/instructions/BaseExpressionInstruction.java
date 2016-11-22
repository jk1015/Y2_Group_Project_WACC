package wacc.instructions;

import java.io.PrintStream;

public abstract class BaseExpressionInstruction extends ExprInstruction {

    private final int register;

    public BaseExpressionInstruction(int register) {
        this.register = register;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR r" + register + ", ");
    }

    @Override
    public String getLocationString() {
        return "r" + register;
    }
}
