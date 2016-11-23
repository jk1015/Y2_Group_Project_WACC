package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

import java.util.ArrayList;

public abstract class ContainingDataOrLabelsInstruction implements Instruction {
    protected ArrayList<DataInstruction> data = new ArrayList<>();
    protected ArrayList<LabelInstruction> labels = new ArrayList<>();

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
        Type type = exprIns.getType();

        if (type.checkType(PrimType.CHAR)) {
            return "putchar";
        } else {
            if (type.checkType(PrimType.INT)) {
                return string + "int";
            } else if (type.checkType(PrimType.BOOL)) {
                return string + "bool";
            } else {
                return string + "string";
            }
        }
    }
}
