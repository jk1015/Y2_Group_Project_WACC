package wacc.instructions;

import wacc.types.Type;

import java.io.PrintStream;

public class IdentifierInstruction implements LocatableInstruction {

    private final String location;
    private final Type type;

    public IdentifierInstruction(String location, Type type) {
        this.location = location;
        this.type = type;
    }


    @Override
    public void toAssembly(PrintStream out) {}

    @Override
    public String getLocationString() {
        return location;
    }

    @Override
    public Type getType() {
        return type;
    }
}
