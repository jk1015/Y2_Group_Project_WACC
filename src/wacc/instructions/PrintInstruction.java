package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;

public class PrintInstruction extends ContainingDataOrLabelsInstruction {

    private ExprInstruction expr;
    private int numOfMsg;
    private String nameOfLabel;
    private Type type;

    public PrintInstruction(ExprInstruction expr, int numOfMsg) {
        this.expr = expr;
        this.numOfMsg = numOfMsg;
        this.nameOfLabel = getType("p_print_",expr);
        this.type = expr.getType();
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("BL " + nameOfLabel);
        addDataAndLabels();
    }

    public void addDataAndLabels() {
        if (type.checkType(PrimType.INT)) {
            String ascii = "\0";
            String nameOfMsg = "msg" + numOfMsg;
            DataInstruction dataInstruction = new DataInstruction(nameOfMsg, ascii);
            addData(dataInstruction);
            String[] namesOfMsg = {nameOfMsg};
            LabelInstruction labelInstruction = new LabelInstruction(nameOfLabel, namesOfMsg);
            addlebel(labelInstruction);
        } else {
            String ascii1 = "true\0";
            String ascii2 = "false\0";
            String nameOfMsg1 = "msg" + numOfMsg;
            String nameOfMsg2 = "msg" + (numOfMsg + 1);

            if (type.checkType(PrimType.STRING)) {
                ascii1 = ((StringLiterInstruction) expr).getStringLiter();
                ascii2 = "%.*s\0";
                nameOfMsg1 = "msg" + numOfMsg;
                nameOfMsg2 = "msg" + (numOfMsg + 1);
            }
            DataInstruction dataInstruction1 = new DataInstruction(nameOfMsg1, ascii1);
            addData(dataInstruction1);
            DataInstruction dataInstruction2 = new DataInstruction(nameOfMsg1, ascii2);
            addData(dataInstruction2);
            String[] namesOfMsg = {nameOfMsg1, nameOfMsg2};
            LabelInstruction labelInstruction = new LabelInstruction(nameOfLabel, namesOfMsg);
            addlebel(labelInstruction);
        }
    }

}
