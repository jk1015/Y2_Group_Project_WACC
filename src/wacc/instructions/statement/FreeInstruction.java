package wacc.instructions.statement;


import wacc.instructions.Instruction;
import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.PairType;

import java.io.PrintStream;

public class FreeInstruction implements Instruction {

    ExprInstruction expr;

    public FreeInstruction(ExprInstruction expr) {
        this.expr = expr;
    }

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
