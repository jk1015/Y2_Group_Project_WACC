package wacc.instructions.expressions.unaryExpressions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class NotInstruction extends ExprInstruction{

    private ExprInstruction expr;

    public NotInstruction(ExprInstruction expr, int register) {
        super(register);
        this.expr = expr;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("EOR " + getLocationString() + ", " + expr.getLocationString() +", #1");
    }
}
