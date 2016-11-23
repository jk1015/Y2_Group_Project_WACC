package wacc.instructions;


import java.util.ArrayList;

public abstract class ContainingDataOrLabelsInstruction implements Instruction {
    protected ArrayList<DataInstruction> data;
    protected ArrayList<LabelInstruction> labels;

    public ArrayList<DataInstruction> getData() {
        return data;
    }

    public ArrayList<LabelInstruction> getLabel() {
        return labels;
    }

    protected void addData(DataInstruction newData){
        data.add(newData);
    }

    protected void addlebel(LabelInstruction newLabel){
        labels.add(newLabel);
    }

    protected String getType(String string, ExprInstruction exprIns) {

        if (exprIns instanceof CharLiterInstruction) {
            return "putchar";
        } else {
            if (exprIns instanceof IntLiterInstruction) {
                return string + "int";
            } else if (exprIns instanceof StringLiterInstruction) {
                return string + "string";
            } else {
                return string + "bool";
            }
        }
    }
}
