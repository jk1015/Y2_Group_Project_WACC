package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.List;

public class CallFunctionInstruction implements LocatableInstruction {

    private String functionLabel;
    private final boolean usesLabel;
    private DerefIdentLHSInstruction ins;
    private List<ExprInstruction> args;

    public CallFunctionInstruction(String functionLabel, List<ExprInstruction> args) {
        this.usesLabel = true;
        this.functionLabel = functionLabel;
        this.args = args;
    }

    public CallFunctionInstruction(DerefIdentLHSInstruction ins, List<ExprInstruction> args) {
        this.usesLabel = false;
        this.ins = ins;
        this.args = args;
    }

    @Override
    public void toAssembly(PrintStream out) {
        // evaluate expressions and store in stack

        if (!usesLabel) {
            ins.toAssembly(out);
        }

        for (ExprInstruction arg : args) {
            //TODO assumed r4 would be free
            //TODO maybe line below is redundant
            arg.toAssembly(out);
            out.println("STR " + arg.getLocationString() + ", [sp, #-4]!");
        }

        // go to function
        if (usesLabel) {
            out.println("BL " + functionLabel);
        } else {
            String loc = ins.getLocationString();
            loc = loc.substring(1, loc.length() - 1);
            out.println("MOV lr, pc");
            out.println("MOV pc, " + loc);
        }

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
