package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

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
        Type type = expr.getType();

        if (type.checkType(PrimType.BOOL)) {

        } else if (type.checkType(PrimType.STRING) {
            String ascii1 = "true\0";
            String ascii2 = "false\0";
            String nameOfMsg1 = "msg" + numOfMsg;
            String nameOfMsg2 = "msg" + (numOfMsg + 1);
            DataInstruction dataInstruction1 = new DataInstruction(nameOfMsg1, ascii1);
            addData(dataInstruction1);
            DataInstruction dataInstruction2 = new DataInstruction(nameOfMsg1, ascii2);
            addData(dataInstruction2);
            String[] namesOfMsg = {nameOfMsg1, nameOfMsg2};
            LabelInstruction labelInstruction = new LabelInstruction(nameOfLabel, namesOfMsg);
            addlebel(labelInstruction);
        }
        } else {
            String ascii = "%.*s\0";
            System.out.println(ascii);
            if (type.checkType(PrimType.INT)) {
                ascii = "\0";
            }
            System.out.println("got here");
            String nameOfMsg = "msg" + numOfMsg;
            DataInstruction dataInstruction = new DataInstruction(nameOfMsg, ascii);
            addData(dataInstruction);
            String[] namesOfMsg = {nameOfMsg};
            LabelInstruction labelInstruction = new LabelInstruction(nameOfLabel, namesOfMsg);
            addlebel(labelInstruction);
            System.out.println(ascii);
        }
    }

}
