package wacc.instructions;

import java.io.PrintStream;

public class ProgramInstruction implements Instruction {

    private final Instruction stat;

    public ProgramInstruction(Instruction stat) {
        this.stat = stat;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println(".text");
        out.println();
        out.println(".global main");
        out.println("main:");
        out.println("PUSH {lr}");
        stat.toAssembly(out);
        out.println("LDR r0, =0");
        out.println("POP {pc}");
        out.println(".ltorg");
    }
}
