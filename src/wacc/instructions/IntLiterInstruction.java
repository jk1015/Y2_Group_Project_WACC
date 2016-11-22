package wacc.instructions;

import java.io.PrintStream;

public class IntLiterInstruction extends BaseExpressionInstruction {

    private int value;

    public IntLiterInstruction(int value, int register) {
        super(register);
        this.value = value;
    }

    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("=" + value);
    }

}
