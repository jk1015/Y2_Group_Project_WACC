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
        out.println("VMOV.I32 d" + currentReg + hex(valueInFloat));

    }

    public static String hex(int n) {
        // call toUpperCase() if that's required
        return String.format("0x%8s", Integer.toHexString(n)).replace(' ', '0');
    }

    public static String hex(float f) {
        // change the float to raw integer bits(according to the OP's requirement)
        return hex(Float.floatToRawIntBits(f));
    }
}
