package wacc.instructions.locatable.expressions.unaryExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class NotInstruction extends ExprInstruction{

    private ExprInstruction expr;

    public NotInstruction(ExprInstruction expr, int register) {
        super(register, PrimType.BOOL);
        this.expr = expr;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("EOR " + getLocationString() + ", " + expr.getLocationString() +", #1");
    }
}
