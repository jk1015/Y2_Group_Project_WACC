package wacc.instructions.statement;


import wacc.instructions.DataInstruction;
import wacc.instructions.Instruction;
import wacc.instructions.locatable.assignment.AssignLHSInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.HashMap;

public class ReadInstruction implements Instruction {
    private final String nameOfLabel;
    private final AssignLHSInstruction lhsInstruction;
    private final Type type;
    private HashMap<String, String> dataMap;
    private ContainingDataOrLabelsInstruction dataAndLabels;

    public ReadInstruction(AssignLHSInstruction assignLHSInstruction,HashMap<String,String> dataMap) {
        this.dataMap = dataMap;
        this.lhsInstruction = assignLHSInstruction;
        this.dataAndLabels = new ContainingDataOrLabelsInstruction(dataMap);
        this.type = assignLHSInstruction.getType();
        this.nameOfLabel = "p_read_" + readType(type);
    }

    private String readType(Type type){
        if (type.checkType(PrimType.CHAR)) {
            return "char";
        } else if (type.checkType(PrimType.INT)) {
                return "int";
        }
        return null;
    }

    @Override
    public void toAssembly(PrintStream out) {
        lhsInstruction.toAssembly(out);
        String offset = lhsInstruction.getOffsetString();
        if(offset == null) {
            out.println("MOV " + "r0, " + lhsInstruction.getLocationString());
            out.println("BL " + nameOfLabel);
        } else {
            out.println("ADD " + "r0" + ", sp, " + offset);
            out.println("BL " + nameOfLabel);
        }
    }

    public HashMap<String,String> addDataAndLabels() {
        if (type.checkType(PrimType.CHAR)) {
            String[] ascii = {"\" %c\\0\""};
            dataMap = dataAndLabels.addDataAndLabels(nameOfLabel,ascii);
        }else if (type.checkType(PrimType.INT)) {
            String[] ascii = {"\"%d\\0\""};
            dataMap = dataAndLabels.addDataAndLabels(nameOfLabel,ascii);
        }
        return dataMap;
        }

    public ContainingDataOrLabelsInstruction getDataAndLabels(){
        return dataAndLabels;
    }
}

