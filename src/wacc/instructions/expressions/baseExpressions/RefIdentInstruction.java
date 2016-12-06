package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;

public class RefIdentInstruction extends ExprInstruction {

    private final String location;

    public RefIdentInstruction(int register, Type type, String location) {
        super(register, type);
        this.location = location;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("LDR " + getLocationString() + ", " + location);
    }
}
