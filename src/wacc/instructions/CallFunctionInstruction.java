package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.List;

public class CallFunctionInstruction implements LocatableInstruction {

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
            arg.toAssembly(out);
            out.println("STR " + arg.getLocationString() + ", [sp, #-4]!");
        }

        // go to function
        out.println("BL " + functionLabel);

        // reset stack pointer
        int totalArgStackSize = args.size() * 4;
        if(totalArgStackSize > 0) {
            out.println("ADD sp, sp, #" + totalArgStackSize);
        }
        // store result of function
        //TODO assumed r4 would be free
        out.println("MOV r4, r0");
    }

    @Override
    public String getLocationString() {
        return "r4";
    }
    //TODO
    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean usesRegister() {
        return true;
    }
}
