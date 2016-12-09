package wacc.instructions;

import wacc.types.PtrType;
import wacc.types.Type;

import java.io.PrintStream;

public class DerefIdentLHSInstruction implements LocatableInstruction {

    private final String locationString;
    private final Type type;
    private final int currentReg;
    private final int derefNum;
    private final LocatableInstruction ins;

    public DerefIdentLHSInstruction(LocatableInstruction ins, String location, Type type, int currentReg, int derefNum) {
        this.locationString = location;
        this.type = type;
        this.currentReg = currentReg;
        this.derefNum = derefNum;
        this.ins = ins;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
        out.println("LDR r" + currentReg + ", " + locationString);
        for (int i = 0; i < derefNum - 1; i++) {
            out.println("LDR r" + currentReg + ", [r" + currentReg + "]");
        }
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
        System.out.println("TRUE");
        return true;
    }


}
