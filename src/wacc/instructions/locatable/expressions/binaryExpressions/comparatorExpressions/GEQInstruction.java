package wacc.instructions.locatable.expressions.binaryExpressions.comparatorExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class GEQInstruction extends ComparatorExprInstruction {


    public GEQInstruction(ExprInstruction expr1, ExprInstruction expr2, int register) {
        super("MOVGE ", "MOVLT ", expr1, expr2, register);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
    }
}
