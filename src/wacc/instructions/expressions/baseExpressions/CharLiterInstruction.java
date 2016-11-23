package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class CharLiterInstruction extends ExprInstruction {


    private char value;

    public CharLiterInstruction(char value, int register) {
        super(register, PrimType.CHAR);
        this.value = value;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("MOV " + getLocationString() + ", ");
        out.println("#'" + value + "'");

    }
}
