package wacc.instructions.statement;

import wacc.instructions.Instruction;

import java.io.PrintStream;

public class SequenceInstruction implements Instruction {

    private final Instruction ins1;
    private final Instruction ins2;

    public SequenceInstruction(Instruction ins1, Instruction ins2) {
        this.ins1 = ins1;
        this.ins2 = ins2;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins1.toAssembly(out);
        ins2.toAssembly(out);
    }
}
