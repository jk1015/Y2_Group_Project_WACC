package wacc.instructions.expressions.binaryExpressions.comparatorExpressions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class LTInstruction extends ComparatorExprInstruction {


    public LTInstruction(ExprInstruction expr1, ExprInstruction expr2, int register) {
        super("MOVLT ", "MOVGE ", expr1, expr2, register);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
    }
}
