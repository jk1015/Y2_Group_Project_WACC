package wacc.instructions;

import java.io.PrintStream;

public class ProgramInstruction implements Instruction {

    private final Instruction stat;
    private final int scopeSize;

    public ProgramInstruction(Instruction stat, int scopeSize) {
        this.stat = stat;
        this.scopeSize = scopeSize;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println(".text");
        out.println();
        out.println(".global main");
        out.println("main:");
        out.println("PUSH {lr}");
        stat.toAssembly(out);
        if (scopeSize > 0) {
            out.println("ADD sp, sp, #" + scopeSize);
        }
        out.println("LDR r0, =0");
        out.println("POP {pc}");
        out.println(".ltorg");
    }
}
