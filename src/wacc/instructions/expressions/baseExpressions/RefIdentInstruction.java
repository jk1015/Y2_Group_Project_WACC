package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;

public class RefIdentInstruction extends ExprInstruction {

    private final int stackOffset;

    public RefIdentInstruction(int register, Type type, int stackOffset) {
        super(register, type);
        this.stackOffset = stackOffset;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("ADD " + getLocationString() + ", sp, #" + stackOffset);
    }
}
