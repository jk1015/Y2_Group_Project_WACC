package wacc.instructions.expressions.unaryExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class LenInstruction extends ExprInstruction {

    private ExprInstruction expr;

    public LenInstruction(ExprInstruction expr, int register) {
        super(register, PrimType.INT);
        this.expr = expr;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("LDR " + getLocationString() + ", [" + expr.getLocationString() +"]");
    }
}
