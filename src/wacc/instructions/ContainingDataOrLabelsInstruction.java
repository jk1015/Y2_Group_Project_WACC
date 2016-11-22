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
}
