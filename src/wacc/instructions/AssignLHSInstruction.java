package wacc.instructions;

import wacc.types.Type;

import java.io.PrintStream;

public class AssignLHSInstruction implements LocatableInstruction {


    private final LocatableInstruction ins;
    private boolean isOnHeap;

    public AssignLHSInstruction(LocatableInstruction ins) {
        this.ins = ins;
        this.isOnHeap = !(ins instanceof IdentifierInstruction);
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
    }

    @Override
    public String getLocationString() {
        return ins.getLocationString();
    }

    public String getOffsetString() {
        if (!isOnHeap) {
            return ((IdentifierInstruction) ins).getOffsetString();
        } else {
            return null;
        }
    }

    @Override
    public Type getType() {
        return ins.getType();
    }
}
