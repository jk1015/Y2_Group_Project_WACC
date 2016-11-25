package wacc.instructions.locatable.expressions.unaryExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 23/11/16.
 */
public class ChrInstruction extends ExprInstruction {

    private ExprInstruction expr;

    public ChrInstruction(ExprInstruction expr, int register) {
        super(register, PrimType.CHAR);
        this.expr = expr;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
    }
}
