package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.ArrayType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.List;

public class ArrayLiterInstruction implements LocatableInstruction {

    private final List<ExprInstruction> elems;

    public ArrayLiterInstruction(List<ExprInstruction> elems) {
        this.elems = elems;
    }

    @Override
    public void toAssembly(PrintStream out) {
        // sets number of bytes required for allocation
        out.println("LDR r0 =" + (elems.size()+1) * 4);

        // TODO write malloc at end
        out.println("BL malloc");

        out.println("MOV r4, r0");

        for (int i = 1; i <= elems.size(); i++) {
            ExprInstruction current = elems.get(i-1);
            current.toAssembly(out);
            out.println("STR " + current.getLocationString() + ", [r4, #" + i*4 + ']');
        }

        out.println("LDR r5, =" + elems.size());
    }

    @Override
    public String getLocationString() {
        return "r5";
    }

    @Override
    public Type getType() {
        return new ArrayType(elems.get(0).getType());
    }
}
