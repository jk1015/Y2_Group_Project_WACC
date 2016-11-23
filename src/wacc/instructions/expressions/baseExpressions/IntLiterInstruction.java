package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class IntLiterInstruction extends ExprInstruction {

    private int value;

    public IntLiterInstruction(int value, int register) {
        super(register);
        this.value = value;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=" + value);
    }

}
