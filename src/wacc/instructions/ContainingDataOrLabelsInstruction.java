package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.ArrayList;

public abstract class ContainingDataOrLabelsInstruction implements Instruction {
    protected ArrayList<DataInstruction> data = new ArrayList<>();
    protected ArrayList<LabelInstruction> labels = new ArrayList<>();

    protected ExprInstruction expr;
    protected int numOfMsg;
    protected String nameOfLabel;
    protected Type type;

    protected abstract int addDataAndLabels();

    ContainingDataOrLabelsInstruction(){

    }
    ContainingDataOrLabelsInstruction(ExprInstruction expr, int numOfMsg){
        this.expr = expr;
        this.numOfMsg = numOfMsg;
        this.type = expr.getType();
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


    public void setLabel(String label, String[] namesOfMsg) {
        LabelInstruction labelInstruction = new LabelInstruction(label, namesOfMsg);
        addlebel(labelInstruction);
    }

    public String setData(String ascii) {
        String nameOfMsg = "msg" + numOfMsg;
        DataInstruction dataInstruction = new DataInstruction(nameOfMsg, ascii);
        addData(dataInstruction);
        numOfMsg++;
        return nameOfMsg;
    }

}
