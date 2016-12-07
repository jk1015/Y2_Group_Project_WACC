package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.IdentifierInstruction;
import wacc.instructions.Instruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.StructType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 07/12/16.
 */
public class StructContentsExprInstruction extends ExprInstruction {

    private final IdentifierInstruction getStructIns;
    private final int offset;
    private final int currentReg;

    public StructContentsExprInstruction(IdentifierInstruction getStructIns, StructType struct, String fieldId, int currentReg) {
        super(currentReg, struct.getType(fieldId));

        this.getStructIns = getStructIns;
        this.offset = struct.getOffset(fieldId);
        this.currentReg = currentReg;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("LDR r" + currentReg + ", " + getStructIns.getLocationString());
        out.println("LDR r" + currentReg + ", " + "[r" + currentReg + ", #" + offset + "]");
    }
}
