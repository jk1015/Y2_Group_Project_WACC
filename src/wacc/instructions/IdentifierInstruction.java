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

    public String getOffsetString() {
        if (location.length() == 4) {
            return "#0";
        } else {
            return "#" + location.substring(7, location.length() - 1);
        }
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean usesRegister() {
        return false;
    }
}
