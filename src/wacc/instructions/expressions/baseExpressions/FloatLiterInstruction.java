package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.Instruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by ad5115 on 01/12/16.
 */
public class FloatLiterInstruction extends ExprInstruction {
    private float valueInFloat;
    private int currentReg;

    public FloatLiterInstruction(float valueInFloat, int currentReg) {
        super(currentReg, PrimType.FLOAT);
        this.valueInFloat = valueInFloat;
        this.currentReg = currentReg;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("FLD" + valueInFloat);

    }
}
