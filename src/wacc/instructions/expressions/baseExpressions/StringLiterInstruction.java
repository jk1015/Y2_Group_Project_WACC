package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class StringLiterInstruction extends ExprInstruction {

    private int register;
    private int location;
    //TODO
    //print string at top of file
    public StringLiterInstruction(int count, String value, int register) {
        super(register);
        this.register = register;
        this.location = count;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=msg_" + location);
    }
}
