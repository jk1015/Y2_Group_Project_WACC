package wacc.instructions.statement;


import wacc.instructions.DataInstruction;
import wacc.instructions.Instruction;
import wacc.instructions.LabelInstruction;
import wacc.instructions.locatable.assignment.AssignLHSInstruction;
import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.ArrayList;

public  class ContainingDataOrLabelsInstruction implements Instruction {
    public ArrayList<DataInstruction> data = new ArrayList<>();
    public ArrayList<LabelInstruction> labels = new ArrayList<>();

    public ExprInstruction expr;
    public int numOfMsg;
    public String nameOfLabel;
    public Type type;

    ContainingDataOrLabelsInstruction(AssignLHSInstruction assignLHSInstruction, int numOfMsg){
        this.numOfMsg = numOfMsg;
        this.type = assignLHSInstruction.getType();

    }
    ContainingDataOrLabelsInstruction(ExprInstruction expr, int numOfMsg){
        this.expr = expr;
        this.numOfMsg = numOfMsg;
        this.type = expr.getType();
    }

    public ContainingDataOrLabelsInstruction(int numOfMsg) {
        this.numOfMsg = numOfMsg;
    }

    public ContainingDataOrLabelsInstruction() {

    }

    public ArrayList<DataInstruction> getData() {

        return data;
    }

    public ArrayList<LabelInstruction> getLabel() {

        return labels;
    }

    public void addData(DataInstruction newData){
        data.add(newData);
    }

    public void addLabel(LabelInstruction newLabel){

        labels.add(newLabel);
    }

    public void setLabel(String label, String[] namesOfMsg) {
        LabelInstruction labelInstruction = new LabelInstruction(label, namesOfMsg);
        addLabel(labelInstruction);
    }

    public String setData(String name,String ascii) {
        DataInstruction dataInstruction = new DataInstruction(name, ascii);
        addData(dataInstruction);
        return name;
    }

    @Override
    public void toAssembly(PrintStream out) {

    }
}
