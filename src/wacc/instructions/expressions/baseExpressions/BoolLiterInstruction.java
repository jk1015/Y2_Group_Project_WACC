package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class BoolLiterInstruction extends ExprInstruction {

    private int value;

    public BoolLiterInstruction(boolean value, int register) {
        super(register, PrimType.BOOL);
        if(value) {
            this.value = 1;
        } else {
            this.value = 0;
        }
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("MOV " + getLocationString() + ", ");
        out.println("#" + value);

    }

    public boolean getValue() {
        return value == 1;
    }
}
