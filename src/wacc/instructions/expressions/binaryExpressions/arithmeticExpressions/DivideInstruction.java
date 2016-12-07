package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.FloatLiterInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class DivideInstruction extends ArithmeticInstruction {

    public DivideInstruction(ExprInstruction expr1, ExprInstruction expr2, int register,int numOfMsg) {
        super(expr1, expr2, register, PrimType.INT,numOfMsg);

    }


    @Override
    public void assembly(PrintStream out) {
            out.println("MOV r0, " + getExpr1String());
            out.println("MOV r1, " + getExpr2String());
            out.println("BL p_check_divide_by_zero");
            out.println("BL __aeabi_idiv");
            out.println("MOV " + getLocationString() + ", r0");
    }

    @Override
    protected float operate(float f1, float f2) {
        return f1 / f2;
    }

    @Override
    public int setCheckError() {
        if (!(expr1 instanceof FloatLiterInstruction ||
                expr2 instanceof FloatLiterInstruction)) {
            numOfMsg = addDataAndLabels("p_check_divide_by_zero", "\"DivideByZeroError:divide or modulo by zero\\n\\0\"");
            numOfMsg = addDataAndLabels("p_throw_runtime_error", "\"DivideByZeroError:divide or modulo by zero\\n\\0\"");
            numOfMsg = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        }
        return numOfMsg;
    }

}
