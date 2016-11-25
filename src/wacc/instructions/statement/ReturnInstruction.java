package wacc.instructions.statement;

import wacc.instructions.Instruction;
import wacc.instructions.locatable.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 24/11/16.
 */
public class ReturnInstruction implements Instruction {

    private final ExprInstruction expr;

    public ReturnInstruction(ExprInstruction expr) {
        this.expr = expr;
    }


    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("MOV r0, " + expr.getLocationString());
    }
}
