package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;

public class DerefIdentInstruction extends ExprInstruction {

    private final String location;
    private final int derefNum;

    public DerefIdentInstruction(int register, Type type, String location, int derefNum) {
        super(register, type);
        this.location = location;
        this.derefNum = derefNum;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("LDR " + getLocationString() + ", [sp, #" + location + "]");
        for (int i = 0; i < derefNum; i++) {
            out.println("LDR " + getLocationString() + ", [r" + location + "]");
        }
    }
}
