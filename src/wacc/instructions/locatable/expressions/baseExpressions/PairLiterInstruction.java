package wacc.instructions.locatable.expressions.baseExpressions;

import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.NullType;
import wacc.types.PairType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class PairLiterInstruction extends ExprInstruction {


    public PairLiterInstruction(int register) {
        super(register, new PairType(new NullType(), new NullType()));
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=0");
    }
}
