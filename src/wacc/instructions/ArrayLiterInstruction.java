package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.ArrayType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.List;

public class ArrayLiterInstruction implements LocatableInstruction {

    private final List<ExprInstruction> elems;
    private final int currentReg;

    public ArrayLiterInstruction(List<ExprInstruction> elems, int currentReg) {
        this.elems = elems;
        this.currentReg = currentReg;
    }

    @Override
    public void toAssembly(PrintStream out) {
        // sets number of bytes required for allocation
        out.println("LDR r0, =" + (elems.size()+1) * 4);

        out.println("BL malloc");

        out.println("MOV r" + currentReg + ", r0");

        for (int i = 1; i <= elems.size(); i++) {
            ExprInstruction current = elems.get(i-1);
            current.toAssembly(out);
            out.println("STR " + current.getLocationString() + ", [r" + currentReg + ", #" + i*4 + ']');
        }

        out.println("LDR r" + (currentReg + 1) +", =" + elems.size());
        out.println("STR r" + (currentReg + 1) + ", " + "[" + "r" + currentReg + "]");
    }

    @Override
    public String getLocationString() {
        return "r" + currentReg;
    }

    @Override
    public Type getType() {
        return new ArrayType(elems.get(0).getType());
    }
}
