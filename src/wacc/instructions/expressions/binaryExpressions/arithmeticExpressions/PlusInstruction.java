package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.FloatLiterInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Created by jk1015 on 22/11/16.
 */
public class PlusInstruction extends ArithmeticInstruction {
    private final int MAX = (2^31) - 1;
    private boolean tooBig;

    public PlusInstruction(ExprInstruction expr1, ExprInstruction expr2,
                           int register, HashMap<String,String> dataMap) {
        super(expr1, expr2, register, PrimType.INT, dataMap);
        float f = operate(f1,f2);
        if (f > MAX){
            tooBig = true;
        }
    }


    @Override
    public void assembly(PrintStream out) {
            out.println("ADDS " + getLocationString() + ", "
                    + getExpr1String() + ", " + getExpr2String());
            out.println("BLVS p_throw_overflow_error");
    }

    @Override
    protected float operate(float f1, float f2) {
        return f1 + f2;
    }


    @Override
    public HashMap<String,String> setCheckError() {
        if (!(expr1 instanceof FloatLiterInstruction ||
                expr2 instanceof FloatLiterInstruction)
                || tooBig) {
            dataMap = addDataAndLabels("p_throw_overflow_error",
                    "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
            dataMap = addDataAndLabels("p_throw_runtime_error",
                    "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
            dataMap = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        }
        return dataMap;

    }

}