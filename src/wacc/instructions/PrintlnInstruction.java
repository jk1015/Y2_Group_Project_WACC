package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class PrintlnInstruction extends ContainingDataOrLabelsInstruction {
    private PrintInstruction printInstruction;
    private int numOfMsg;

    public PrintlnInstruction(ExprInstruction expr, int numOfMsg) {
        printInstruction = new PrintInstruction(expr, numOfMsg);
        this.numOfMsg = numOfMsg;
    }

    @Override
    public void toAssembly(PrintStream out) {
        printInstruction.toAssembly(out);

        String nameOfLabel = "p_print_ln";
        out.println("BL " + nameOfLabel);
        String nameOfMsg1 = "msg" + numOfMsg + 2;
        DataInstruction dataInstruction1 = new DataInstruction(nameOfMsg1,"\0");
        data.add(dataInstruction1);
        String[] namesOfMsg = {nameOfMsg1};
        LabelInstruction labelInstruction = new LabelInstruction(nameOfLabel,namesOfMsg);
        labels.add(labelInstruction);
    }
}
