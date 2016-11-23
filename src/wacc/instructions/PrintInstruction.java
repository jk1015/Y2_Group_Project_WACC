package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;

public class PrintInstruction extends ContainingDataOrLabelsInstruction {

    protected ExprInstruction expr;
    protected int numOfMsg;
    protected String nameOfLabel;
    protected Type type;

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

    public int addDataAndLabels() {
        if (!type.checkType(PrimType.CHAR)) {
            if (type.checkType(PrimType.INT)) {
                String nameOfMsg = setData("%d\0");
                String[] namesOfMsg = {nameOfMsg};
                setLabel(nameOfLabel, namesOfMsg);
            } else {
                String nameOfMsg1;
                String nameOfMsg2;

                if (type.checkType(PrimType.STRING)) {
                    nameOfMsg1 = setData(((StringLiterInstruction) expr).getStringLiter());
                    nameOfMsg2 = setData("%.*s\0");

                } else {
                    nameOfMsg1 = setData("true\0");
                    nameOfMsg2 = setData("false\0");
                }
                String[] namesOfMsg = {nameOfMsg1, nameOfMsg2};
                setLabel(nameOfLabel, namesOfMsg);
            }
        }
        return numOfMsg;
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
