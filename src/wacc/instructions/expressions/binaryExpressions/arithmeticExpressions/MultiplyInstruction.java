package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.FloatLiterInstruction;
import wacc.instructions.expressions.baseExpressions.IntLiterInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class MultiplyInstruction extends BinaryExprInstruction {

    private int extraReg;

    public MultiplyInstruction(ExprInstruction expr1, ExprInstruction expr2, int register1, int register2,int numOfMsg) {
        super(expr1, expr2, register1, PrimType.INT,numOfMsg);
        this.extraReg = register2;
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        if (expr1 instanceof FloatLiterInstruction &&
                expr2 instanceof FloatLiterInstruction) {
            out.println("FMULP r0");
        }else if(expr1 instanceof FloatLiterInstruction) {
            out.println("FLD " + ((IntLiterInstruction)expr2).getValue());
            out.println("FMULP r0");
        }else if(expr2 instanceof FloatLiterInstruction){
            out.println("FLD " + ((IntLiterInstruction)expr1).getValue());
            out.println("FMULP r0");
        }else {
            out.println("SMULL " + getLocationString() + ", r" + extraReg + ", " + getExpr1String() + ", " + getExpr2String());
            out.println("CMP r" + extraReg + ", " + getLocationString() + ", ASR #31");
            out.println("BLNE p_throw_overflow_error");
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
