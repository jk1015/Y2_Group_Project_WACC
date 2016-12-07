package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.FloatLiterInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class MultiplyInstruction extends ArithmeticInstruction {

    private int extraReg;

    public MultiplyInstruction(ExprInstruction expr1, ExprInstruction expr2, int register1, int register2,int numOfMsg) {
        super(expr1, expr2, register1, PrimType.INT,numOfMsg);
        this.extraReg = register2;
    }


    @Override
    public void assembly(PrintStream out) {
            out.println("SMULL " + getLocationString() + ", r" + extraReg + ", " + getExpr1String() + ", " + getExpr2String());
            out.println("CMP r" + extraReg + ", " + getLocationString() + ", ASR #31");
            out.println("BLNE p_throw_overflow_error");
    }

    @Override
    protected float operate(float f1, float f2) {
        return f1 * f2;
    }

    @Override
    public int setCheckError() {
        if (!(expr1 instanceof FloatLiterInstruction ||
                expr2 instanceof FloatLiterInstruction)) {
            numOfMsg = addDataAndLabels("p_throw_overflow_error",
                    "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
            numOfMsg = addDataAndLabels("p_throw_runtime_error",
                    "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
            numOfMsg = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        }
        return numOfMsg;
    }
}
