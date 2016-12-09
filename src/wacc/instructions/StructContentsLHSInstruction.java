package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.StructType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by jk1015 on 07/12/16.
 */
public class StructContentsLHSInstruction implements LocatableInstruction {


    private final ExprInstruction getStructIns;
    private final String currentReg;
    private final List<String> fieldIds;
    private final Type type;

    public StructContentsLHSInstruction(ExprInstruction getStructIns, List<String> fieldIds) {

        this.getStructIns = getStructIns;
        this.fieldIds = fieldIds;
        this.currentReg = getStructIns.getLocationString();
        StructType struct = (StructType) getStructIns.getType();
        for(int i = 0; i < fieldIds.size() - 1; i++) {
            struct = (StructType) struct.getType(fieldIds.get(i));
        }
        this.type = struct.getType(fieldIds.get(fieldIds.size() - 1));

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

    }

    @Override
    public String getLocationString() {
        return "[" + currentReg + "]";
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean usesRegister() {
        return true;
    }
}
