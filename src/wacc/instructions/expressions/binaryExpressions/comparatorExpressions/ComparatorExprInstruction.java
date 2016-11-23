package wacc.instructions.expressions.binaryExpressions.comparatorExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class ComparatorExprInstruction extends BinaryExprInstruction {

    private String trueCon, falseCon;

    public ComparatorExprInstruction(String trueCon, String falseCon,
                                     ExprInstruction expr1, ExprInstruction expr2, int register) {
        super(expr1, expr2, register, PrimType.BOOL);
        this.trueCon = trueCon;
        this.falseCon = falseCon;
    }


    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("CMP " + getExpr1String() + ", " + getExpr2String());
        out.println(trueCon + getLocationString() + ", #1");
        out.println(falseCon + getLocationString() + ", #0");
    }
}
