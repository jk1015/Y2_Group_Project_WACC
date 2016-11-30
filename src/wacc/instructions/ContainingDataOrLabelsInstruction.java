package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.ArrayList;

public  class ContainingDataOrLabelsInstruction implements Instruction {
    protected ArrayList<DataInstruction> data = new ArrayList<>();
    protected ArrayList<LabelInstruction> labels = new ArrayList<>();

    protected ExprInstruction expr;
    protected int numOfMsg;
    protected String nameOfLabel;
    protected Type type;

    ContainingDataOrLabelsInstruction(AssignLHSInstruction assignLHSInstruction,int numOfMsg){
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

    protected void addData(DataInstruction newData){
        data.add(newData);
    }

    protected void addLabel(LabelInstruction newLabel){

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
