package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.NullType;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class IdentifierExprInstruction extends ExprInstruction {

    private String stringValue;
    private String var;

    //Takes string from MemoryStack getOffsetString
    public IdentifierExprInstruction(String var, Type t, int register) {
        //TODO
        super(register, t);
        this.var = var;
    }

    public IdentifierExprInstruction(String var, Type t, int register,String stringValue) {
        super(register, t);
        this.var = var;
        this.stringValue = stringValue;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println(var);
    }

    public String getStringValue() {
        return stringValue;
    }
}
