package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class StringLiterInstruction extends ExprInstruction {

    private int location;
    //TODO
    //print string at top of file
    public StringLiterInstruction(int count, int register) {
        super(register, PrimType.STRING);
        this.location = count;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=msg_" + location);
    }
}
