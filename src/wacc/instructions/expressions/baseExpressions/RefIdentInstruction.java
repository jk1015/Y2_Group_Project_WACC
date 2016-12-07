package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.LocatableInstruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;

public class RefIdentInstruction extends ExprInstruction {

    private final String location;
    private final LocatableInstruction ins;

    public RefIdentInstruction(int register, LocatableInstruction ins) {
        super(register, ins.getType());
        this.location = ins.getLocationString();
        this.ins = ins;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
        out.println("LDR " + getLocationString() + ", " + location);
    }
}
