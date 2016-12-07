package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.AssignLHSInstruction;
import wacc.instructions.LocatableInstruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PtrType;
import wacc.types.Type;

import java.io.PrintStream;

public class RefIdentInstruction extends ExprInstruction {

    private final String location;
    private final AssignLHSInstruction ins;

    public RefIdentInstruction(int register, AssignLHSInstruction ins) {
        super(register, new PtrType(ins.getType()));

        this.location = ins.getLocationString();
        this.ins = ins;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
        if (ins.isOnHeap()) {
            out.println("MOV " + getLocationString() + ", " + location.substring(1,location.length() -1));
        } else {
            out.println("ADDS " + getLocationString() + ", sp, " + ins.getOffsetString());
        }
    }
}
