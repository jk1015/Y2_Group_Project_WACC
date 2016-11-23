package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class PlusInstruction extends BinaryExprInstruction {


    public PlusInstruction(ExprInstruction expr1, ExprInstruction expr2, int register) {
        super(expr1, expr2, register, PrimType.INT);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("ADDS " + getLocationString() + ", "
                    + getExpr1String() + ", " + getExpr2String());
        //TODO Implement error
        //out.println("BLVS p_throw_overflow_error");
    }
}
