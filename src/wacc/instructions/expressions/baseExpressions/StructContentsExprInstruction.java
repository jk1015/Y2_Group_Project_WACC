package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.IdentifierInstruction;
import wacc.instructions.Instruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.StructType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by jk1015 on 07/12/16.
 */
public class StructContentsExprInstruction extends ExprInstruction {

    private final ExprInstruction getStructIns;
    private final String currentReg;
    private final List<String> fieldIds;
    private final int exprReg;

    public StructContentsExprInstruction(ExprInstruction getStructIns, List<String> fieldIds, int currentReg) {
        super(currentReg, null);

        this.getStructIns = getStructIns;
        this.fieldIds = fieldIds;
        this.currentReg = getStructIns.getLocationString();
        this.exprReg = currentReg;
        StructType struct = (StructType) getStructIns.getType();
        for(int i = 0; i < fieldIds.size() - 1; i++) {
            struct = (StructType) struct.getType(fieldIds.get(i));
        }
        Type type = struct.getType(fieldIds.get(fieldIds.size() - 1));
        super.setType(type);
    }

    @Override
    public void toAssembly(PrintStream out) {
        getStructIns.toAssembly(out);
        StructType struct = (StructType) getStructIns.getType();

        for(int i = 0; i < fieldIds.size() - 1; i++) {
            int offset = struct.getOffset(fieldIds.get(i));
            struct = (StructType) struct.getType(fieldIds.get(i));
            out.println("ADDS " + currentReg +", " + currentReg + ", #" + offset);
        }

        int offset = struct.getOffset(fieldIds.get(fieldIds.size() - 1));
        out.println("ADDS " + currentReg +", " + currentReg + ", #" + offset);
        if(!(getType() instanceof StructType)) {
            out.println("LDR " + currentReg + ", [" + currentReg + "]");
        }
        out.println("MOV r" + exprReg + ", " + currentReg);

    }
}
