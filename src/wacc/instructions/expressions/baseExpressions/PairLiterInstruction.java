package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class PairLiterInstruction extends ExprInstruction {

    private int register;

    public PairLiterInstruction(int value, int register) {
        super(register);
        this.register = register;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=0");
    }
}
