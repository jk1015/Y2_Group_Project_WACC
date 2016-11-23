package wacc.instructions;

import java.io.PrintStream;

public class AssignLHSInstruction implements Instruction {


    private final Instruction ins;

    public AssignLHSInstruction(Instruction ins) {
        this.ins = ins;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
    }
}
