package wacc.instructions.expressions.binaryExpressions.logicalExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class ANDInstruction extends BinaryExprInstruction {


    public ANDInstruction(ExprInstruction expr1, ExprInstruction expr2, int register) {
        super(expr1, expr2, register);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println(" " + getLocationString() + ", "
                + getExpr1String() + ", " + getExpr2String());
    }
}
