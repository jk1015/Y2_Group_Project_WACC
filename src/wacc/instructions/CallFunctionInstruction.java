package wacc.instructions;

import java.io.PrintStream;
import java.util.List;

public class CallFunctionInstruction implements Instruction {

    private String functionLabel;
    private List<Instruction> args;

    public CallFunctionInstruction(String functionLabel, List<Instruction> args) {
        this.functionLabel = functionLabel;
        this.args = args;
    }

    @Override
    public void toAssembly(PrintStream out) {
        // evaluate expressions and store in stack
        for (Instruction arg : args) {
            arg.toAssembly(out);
            out.print("STR r4, [sp, #-4]!");
        }

        // go to function
        out.println("BL " + functionLabel);

        // reset stack pointer
        int totalArgStackSize = args.size() * 4;
        out.println("ADD sp, sp, #" + totalArgStackSize);

        // store result of function
        out.println("MOV r4, r0");
    }
}
