package wacc.instructions;

import wacc.types.StructType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 08/12/16.
 */
public class StructAssignInstruction implements Instruction {

    private final AssignLHSInstruction lhs;
    private final LocatableInstruction rhs;
    private int currentReg;

    public StructAssignInstruction(AssignLHSInstruction lhs, LocatableInstruction rhs, int currentReg) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.currentReg = currentReg;
    }


    @Override
    public void toAssembly(PrintStream out) {
        lhs.toAssembly(out);
        rhs.toAssembly(out);
        String lLoc = lhs.getLocationString();
        String rLoc = rhs.getLocationString();
        if(!lhs.isOnHeap()){
            out.println("LDR r" + currentReg + ", " + lhs.getLocationString());
            lLoc = "r" + currentReg;
            currentReg++;
        } else {
            lLoc = lLoc.substring(1, lLoc.length() - 1);
        }


        StructType struct = (StructType) lhs.getType();
        for(int i = 0; i < struct.getSize(); i +=4) {
            out.println("LDR r" + currentReg + ", [" + rLoc + ", #" + i + "]");
            out.println("STR r" + currentReg + ", [" + lLoc + ", #" + i + "]");
        }

    }
}
