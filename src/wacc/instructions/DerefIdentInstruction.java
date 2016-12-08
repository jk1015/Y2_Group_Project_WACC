package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;

public class DerefIdentInstruction extends ExprInstruction {

    private final String location;
    private final int derefNum;
    private final LocatableInstruction ins;

    public DerefIdentInstruction(int register, String location, Type type, int derefNum, LocatableInstruction ins) {
        super(register, type);
        this.location = location;
        this.derefNum = derefNum;
        this.ins = ins;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
        out.println("LDR " + getLocationString() + ", " + location);
        for (int i = 0; i < derefNum; i++) {
            out.println("LDR " + getLocationString() + ", [" + getLocationString() + "]");
        }
    }
}
