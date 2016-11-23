package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;
import java.util.List;

public class CallFunctionInstruction implements Instruction {

    private String functionLabel;
    private List<ExprInstruction> args;

    public CallFunctionInstruction(String functionLabel, List<ExprInstruction> args) {
        this.functionLabel = functionLabel;
        this.args = args;
    }

    @Override
    public void toAssembly(PrintStream out) {
        // evaluate expressions and store in stack
        for (ExprInstruction arg : args) {
            //TODO assumed r4 would be free
            //TODO maybe line below is redundant
            out.println("LDR r4, " + arg.getLocationString());
            out.print("STR r4, [sp, #-4]!");
        }

        // go to function
        out.println("BL " + functionLabel);

        // reset stack pointer
        int totalArgStackSize = args.size() * 4;
        out.println("ADD sp, sp, #" + totalArgStackSize);

        // store result of function
        //TODO assumed r4 would be free
        out.println("MOV r4, r0");
    }
}
