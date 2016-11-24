package wacc.instructions;


import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;

public class ReadInstruction extends ContainingDataOrLabelsInstruction {
    AssignLHSInstruction lhsInstruction;

    public ReadInstruction(AssignLHSInstruction assignLHSInstruction,int numOfMsg) {
        super(assignLHSInstruction,numOfMsg);
        this.lhsInstruction = assignLHSInstruction;
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

        out.println("ADD " + lhsInstruction.getLocationString() + ", sp, #0");
        out.println("MOV " + " r0," + lhsInstruction.getLocationString());
        out.println("BL " + nameOfLabel);
        out.println("ADD "  + "sp, sp, #4");

    }

    @Override
    public int addDataAndLabels() {
        if (type.checkType(PrimType.CHAR)) {
            String nameOfMsg = setData("\" %c\\0\"");
            String[] namesOfMsg = {nameOfMsg};
            setLabel(nameOfLabel, namesOfMsg);
        }else if (type.checkType(PrimType.INT)) {
                String nameOfMsg = setData("\"%d\\0\"");
                String[] namesOfMsg = {nameOfMsg};
                setLabel(nameOfLabel, namesOfMsg);
        }
        return numOfMsg;
        }

}

