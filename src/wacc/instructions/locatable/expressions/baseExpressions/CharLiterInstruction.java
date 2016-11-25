package wacc.instructions.locatable.expressions.baseExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class CharLiterInstruction extends ExprInstruction {


    private String value;

    public CharLiterInstruction(String value, int register) {
        super(register, PrimType.CHAR);
        this.value = value;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("MOV " + getLocationString() + ", ");
        out.println("#" + value);

    }
}
