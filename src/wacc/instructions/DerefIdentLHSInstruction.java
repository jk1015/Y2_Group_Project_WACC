package wacc.instructions;

import wacc.types.Type;

import java.io.PrintStream;

public class DerefIdentLHSInstruction implements LocatableInstruction {

    private final String locationString;
    private final Type type;
    private final int currentReg;
    private final int derefNum;

    public DerefIdentLHSInstruction(String locationString, Type type, int currentReg, int derefNum) {
        this.locationString = locationString;
        this.type = type;
        this.currentReg = currentReg;
        this.derefNum = derefNum;
    }

    @Override
    public void toAssembly(PrintStream out) {

    }

    @Override
    public String getLocationString() {
        return "[r"+currentReg + "]";
    }

    @Override
    public Type getType() {
        return type;
    }
}
