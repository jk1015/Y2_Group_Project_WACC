package wacc.instructions;

import java.io.PrintStream;

public class AssignInstruction implements Instruction {

    private final LocatableInstruction lhs;
    private final LocatableInstruction rhs;

    public AssignInstruction(LocatableInstruction lhs, LocatableInstruction rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void toAssembly(PrintStream out) {
        lhs.toAssembly(out);
        rhs.toAssembly(out);
        out.println("STR " + rhs.getLocationString() + ", " + lhs.getLocationString());
    }
}
