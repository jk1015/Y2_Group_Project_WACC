package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.ArrayType;
import wacc.types.Type;

import java.io.PrintStream;

/**
 * Created by jaspreet on 08/12/16.
 */
public class NewArrayInstruction implements LocatableInstruction {

    private ExprInstruction expr;
    private int currentReg;
    private Type type;

    public NewArrayInstruction(ExprInstruction expr, int currentReg, Type type) {
        this.expr = expr;
        this.currentReg = currentReg;
        this.type = new ArrayType(type);
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);

        // sets number of bytes required for allocation
        out.println("ADD r0, #1, " + expr.getLocationString() + ", LSL #2");

        out.println("BL malloc");

        out.println("MOV r" + currentReg + ", r0");

        out.println("MOV r" + (currentReg + 2) +", " + expr.getLocationString());
        out.println("STR r" + (currentReg + 2) + ", " + "[" + "r" + currentReg + "]");
    }

    @Override
    public String getLocationString() {
        return "r"+currentReg;
    }

    @Override
    public Type getType() {
        return type;
    }
}
