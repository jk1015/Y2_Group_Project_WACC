package wacc.instructions;

import wacc.types.StructType;
import wacc.types.Type;

import java.io.PrintStream;

/**
 * Created by jk1015 on 07/12/16.
 */
public class StructContentsLHSInstruction implements LocatableInstruction {


    private final IdentifierInstruction getStructIns;
    private final int offset;
    private final Type type;
    private final int currentReg;

    public StructContentsLHSInstruction(IdentifierInstruction getStructIns, StructType struct, String fieldId, int currentReg) {

        this.getStructIns = getStructIns;
        this.offset = struct.getOffset(fieldId);
        this.type = struct.getType(fieldId);
        this.currentReg = currentReg;
    }


    @Override
    public void toAssembly(PrintStream out) {
        out.println("LDR r" + currentReg + ", " + getStructIns.getLocationString());
        out.println("ADDS r" + currentReg +", r" + currentReg + ", #" + offset);
    }

    @Override
    public String getLocationString() {
        return "[r" + currentReg + "]";
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
