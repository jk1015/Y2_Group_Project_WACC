package wacc.instructions.expressions.baseExpressions;

import wacc.BackendVisitor;
import wacc.instructions.DataInstruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by ad5115 on 01/12/16.
 */
public strictfp class FloatLiterInstruction extends ExprInstruction {
    private float valueInFloat;
    private int location;

    public FloatLiterInstruction(int count, int currentReg, float valueInFloat) {
        super(currentReg, PrimType.FLOAT);
        this.valueInFloat = valueInFloat;
        this.location = count;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=msg_" + location);
    }

    public DataInstruction setData(String ascii) {
        String nameOfMsg = "msg_" + location;
        DataInstruction dataInstruction = new DataInstruction(nameOfMsg, ascii);
        return dataInstruction;
    }

    public float getValueInFloat() {
        return valueInFloat;
    }
}
