package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class IdentifierInstruction extends ExprInstruction {

    private String var;

    //Takes string from MemoryStack getLocationString
    public IdentifierInstruction(String var, int register) {
        super(register);
        this.var = var;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println(var);
    }
}
