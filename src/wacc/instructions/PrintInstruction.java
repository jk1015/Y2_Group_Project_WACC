package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class PrintInstruction extends ContainingDataOrLabelsInstruction {

    private ExprInstruction expr;
    private int numOfMsg;

    public PrintInstruction(ExprInstruction expr, int numOfMsg) {
        this.expr = expr;
        this.numOfMsg = numOfMsg;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        String nameOfLabel = getType("p_print_",expr);
        out.println("BL " + nameOfLabel);
        String nameOfMsg1 = "msg" + numOfMsg;
        DataInstruction dataInstruction1 = new DataInstruction(nameOfMsg1,null);
        data.add(dataInstruction1);
        String nameOfMsg2 = "msg" + (numOfMsg+1);
        DataInstruction dataInstruction2 = new DataInstruction(nameOfMsg2, "%.*s\0");
        data.add(dataInstruction2);
        String[] namesOfMsg = {nameOfMsg1, nameOfMsg2};
        LabelInstruction labelInstruction = new LabelInstruction(nameOfLabel,namesOfMsg);
        labels.add(labelInstruction);
    }

}
