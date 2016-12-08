package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Created by jk1015 on 22/11/16.
 */
public class PlusInstruction extends BinaryExprInstruction {


    public PlusInstruction(ExprInstruction expr1, ExprInstruction expr2,
                           int register,HashMap<String,String> dataMap) {
        super(expr1, expr2, register, PrimType.INT,dataMap);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("ADDS " + getLocationString() + ", "
                    + getExpr1String() + ", " + getExpr2String());
        out.println("BLVS p_throw_overflow_error");
    }

    @Override
    public HashMap<String,String> setCheckError() {
        dataMap = addDataAndLabels("p_throw_overflow_error",
                "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
        dataMap = addDataAndLabels("p_throw_runtime_error",
                "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
        dataMap = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        return dataMap;
    }
}
