package wacc.instructions;

import java.io.PrintStream;

public class IdentifierInstruction implements LocatableInstruction {

    private final String location;

    public IdentifierInstruction(String location) {
        this.location = location;
    }


    @Override
    public void toAssembly(PrintStream out) {}

    @Override
    public String getLocationString() {
        return location;
    }
}
