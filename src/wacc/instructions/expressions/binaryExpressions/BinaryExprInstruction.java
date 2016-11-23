package wacc.instructions.expressions.binaryExpressions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public abstract class BinaryExprInstruction extends ExprInstruction {

    private ExprInstruction expr1, expr2;

    public BinaryExprInstruction(ExprInstruction expr1, ExprInstruction expr2, int register) {
        super(register);
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public void toAssembly(PrintStream out) {
        expr1.toAssembly(out);
        expr2.toAssembly(out);
    }

    public String getExpr1String() {
        return expr1.getLocationString();
    }

    public String getExpr2String() {
        return expr2.getLocationString();
    }

}
