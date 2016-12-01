package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.FloatLiterInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class MinusInstruction extends BinaryExprInstruction {


    public MinusInstruction(ExprInstruction expr1, ExprInstruction expr2, int register,int numOfMsg) {
        super(expr1, expr2, register, PrimType.INT,numOfMsg);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        if (expr1 instanceof FloatLiterInstruction ||
                expr2 instanceof FloatLiterInstruction){
            out.println("FLD " + getExpr1String());
            out.println("FLD " + getExpr2String());
            out.println("FSUBP r0");
        }else {
            out.println("SUBS " + getLocationString() + ", "
                    + getExpr1String() + ", " + getExpr2String());
            out.println("BLVS p_throw_overflow_error");
        }
    }

    @Override
    public int setCheckError() {
        numOfMsg = addDataAndLabels("p_throw_overflow_error",
                "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
        numOfMsg = addDataAndLabels("p_throw_runtime_error",
                "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
        numOfMsg = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        return numOfMsg;
    }
}
