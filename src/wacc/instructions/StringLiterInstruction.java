package wacc.instructions;

import java.io.PrintStream;

public class StringLiterInstruction extends BaseExpressionInstruction {
    private String value;

    public StringLiterInstruction(String value, int register) {
        super(register);
        this.value = value;
    }

    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        out.println("=" + value);
    }

}
