package wacc.instructions.locatable.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.instructions.locatable.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Created by jk1015 on 22/11/16.
 */
public class MultiplyInstruction extends BinaryExprInstruction {

    private int extraReg;

    public MultiplyInstruction(ExprInstruction expr1, ExprInstruction expr2, int register1,
                               int register2, HashMap<String,String> dataMap) {
        super(expr1, expr2, register1, PrimType.INT,dataMap);
        this.extraReg = register2;
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("SMULL " + getLocationString() + ", r" + extraReg + ", " + getExpr1String() + ", " + getExpr2String());
        out.println("CMP r" + extraReg + ", " + getLocationString() + ", ASR #31");
        out.println("BLNE p_throw_overflow_error");
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
