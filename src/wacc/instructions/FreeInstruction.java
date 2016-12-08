package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PairType;

import java.io.PrintStream;

public class FreeInstruction implements Instruction {

    ExprInstruction expr;

    public FreeInstruction(ExprInstruction expr) {
        this.expr = expr;
    }
    //TODO: Has this even been implemented?
    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("MOV r0, " + expr.getLocationString());
        if (expr.getType() instanceof PairType) {
            out.println("BL p_free_pair");
        } else {
            out.println("BL p_free_array");
        }
    }
}
