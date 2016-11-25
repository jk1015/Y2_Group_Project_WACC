package wacc.instructions.statement;

import wacc.instructions.Instruction;

import java.io.PrintStream;

public class BlockInstruction implements Instruction {

    private final Instruction ins;
    private final int scopeSize;

    public BlockInstruction(Instruction ins, int scopeSize) {
        this.ins = ins;
        this.scopeSize = scopeSize;
    }

    @Override
    public void toAssembly(PrintStream out) {
        ins.toAssembly(out);
        if (scopeSize > 0) {
            out.println("ADD sp, sp, #" + scopeSize);
        }
    }
}
