package wacc.instructions;

import wacc.types.StructType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk1015 on 08/12/16.
 */
public class StructListInstruction implements LocatableInstruction {


    private final List<LocatableInstruction> assignList;
    private final int currentReg;
    private final Type type;
    private final int size;

    public StructListInstruction(List<LocatableInstruction> assignList, int currentReg) {
        this.assignList = assignList;
        this.currentReg = currentReg;
        List<Type> typeList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        int size = 0;
        for(LocatableInstruction ins : assignList) {
            typeList.add(ins.getType());
            idList.add("");
            if(ins.getType() instanceof StructType) {
                size += ((StructType) ins.getType()).getSize();
            } else {
                size += 4;
            }
        }
        this.size = size;
        this.type = new StructType(StructType.structListName, typeList, idList);

    }


    @Override
    public void toAssembly(PrintStream out) {
        out.println("LDR r0, =" + size);
        out.println("BL malloc");
        out.println("MOV r" + currentReg + ", r0");
        int offset = 0;
        for(LocatableInstruction ins : assignList) {
            ins.toAssembly(out);
            if(ins.getType() instanceof StructType) {
                offset = transferStruct((StructType) ins.getType(), ins.getLocationString(), offset, out);
            } else {
                out.println("STR " + ins.getLocationString() + ", [r" + currentReg + ", #" + offset + "]" );
                offset += 4;
            }

        }
    }

    private int transferStruct(StructType struct, String structBase, int offset, PrintStream out) {
        for(int i = 0; i < struct.getSize(); i += 4) {
            out.println("LDR r" + (currentReg + 2) +", [" + structBase + ", #" + i + "]");
            out.println("STR r" + (currentReg + 2) + ", [r" + currentReg + ", #" + offset + "]");
            offset += 4;
        }
        return offset;
    }


    @Override
    public String getLocationString() {
        return "r" + currentReg;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean usesRegister() {
        return true;
    }
}
