package wacc.instructions.locatable.expressions.binaryExpressions.logicalExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.instructions.locatable.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Created by jk1015 on 22/11/16.
 */
public class ORInstruction extends BinaryExprInstruction {


    public ORInstruction(ExprInstruction expr1, ExprInstruction expr2, int register) {
        super(expr1, expr2, register, PrimType.BOOL);
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("ORR " + getLocationString() + ", "
                + getExpr1String() + ", " + getExpr2String());
    }

    @Override
    public HashMap<String, String> setCheckError() {
        return null;
    }
}
