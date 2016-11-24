package wacc.instructions;

import wacc.types.Type;

import java.io.PrintStream;

public class AssignLHSInstruction implements LocatableInstruction {


    private final LocatableInstruction ins;

    public AssignLHSInstruction(LocatableInstruction ins) {
        this.ins = ins;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
    }

    @Override
    public String getLocationString() {
        return ins.getLocationString();
    }

    @Override
    public Type getType() {
        return ins.getType();
    }
}
