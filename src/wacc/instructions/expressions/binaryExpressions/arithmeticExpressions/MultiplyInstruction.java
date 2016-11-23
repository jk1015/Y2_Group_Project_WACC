package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class MultiplyInstruction extends BinaryExprInstruction {

    private int extraReg;

    public MultiplyInstruction(ExprInstruction expr1, ExprInstruction expr2, int register1, int register2) {
        super(expr1, expr2, register1, PrimType.INT);
        this.extraReg = register2;
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        //TODO Implement error
        out.println("SMULL " + getLocationString() + ", r" + extraReg + ", " + getExpr1String() + ", " + getExpr2String());
        out.println("CMP r" + extraReg + ", " + getLocationString() + ", ASR #31");
        //out.println("BLNE p_throw_overflow_error");
    }
}
