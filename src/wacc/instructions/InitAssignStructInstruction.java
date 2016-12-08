package wacc.instructions;

import wacc.types.StructType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 08/12/16.
 */
public class InitAssignStructInstruction implements Instruction {
    private final LocatableInstruction expr;
    private final String offsetString;

    public InitAssignStructInstruction(LocatableInstruction expr, String offsetString) {
        this.expr = expr;
        this.offsetString = offsetString;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        String eLoc = expr.getLocationString();
        StructType struct = (StructType) expr.getType();
        out.println("LDR r0, =" + struct.getSize());
        out.println("BL malloc");
        out.println("MOV r5, r0");

        for(int i = 0; i < struct.getSize(); i +=4) {
            out.println("LDR r6" + ", [" + eLoc + ", #" + i + "]");
            out.println("STR r6" + ", [r5, #" + i + "]");
        }
        out.println("SUB sp, sp, #4");
        out.println("STR r5, " + offsetString);

    }
}
