package wacc.instructions.locatable.expressions.baseExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

public class IntLiterInstruction extends ExprInstruction {

    private String value;

    public IntLiterInstruction(String value, int register) {
        super(register, PrimType.INT);
        this.value = value;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=" + value);
    }

}
