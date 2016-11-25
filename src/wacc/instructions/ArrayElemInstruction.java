package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.ArrayType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by jaspreet on 25/11/16.
 */
public class ArrayElemInstruction extends ExprInstruction {

    private final String locationString;
    private final Type type;
    private final int currentReg;
    private final List<ExprInstruction> exprs;

    public ArrayElemInstruction(String locationString, Type type, int currentReg, List<ExprInstruction> exprs) {
        super(currentReg, type);
        this.locationString = locationString;
        this.type = type;
        this.currentReg = currentReg;
        this.exprs = exprs;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("LDR r" + currentReg + " " +  locationString);
        for (ExprInstruction expr : exprs) {
            expr.toAssembly(out);
            out.println("MOV r0, r" + currentReg);
            out.println("MOV r1, " + expr.getLocationString());
            out.println("BL p_check_array_bounds");
            out.println("ADD r" + currentReg + ", r" + currentReg + ", #4");
            out.println("ADD r" + currentReg + ", r" + currentReg + " " + expr.getLocationString() + ", LSL #2");
            out.println("LDR r" + currentReg + ", [r" + currentReg + "]");
        }


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
