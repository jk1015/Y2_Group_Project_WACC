package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class ModInstruction extends BinaryExprInstruction {


    public ModInstruction(ExprInstruction expr1, ExprInstruction expr2, int register,int numOfMsg) {
        super(expr1, expr2, register, PrimType.INT,numOfMsg);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("MOV r0, " + getExpr1String());
        out.println("MOV r1, " + getExpr2String());
        //out.println("BL p_check_divide_by_zero");
        out.println("BL __aeabi_idivmod");
        out.println("MOV " + getLocationString() + ", r1");
    }

    @Override
    public int setCheckError() {
        numOfMsg = addDataAndLabels("p_check_divide_by_zero", "\"DivideByZeroError:divide or modulo by zero\\n\\0\"");
        numOfMsg = addDataAndLabels("p_throw_runtime_error", "\"DivideByZeroError:divide or modulo by zero\\n\\0\"");
        numOfMsg = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        return numOfMsg;
    }
}
