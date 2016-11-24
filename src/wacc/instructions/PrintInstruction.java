package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;

public class PrintInstruction extends ContainingDataOrLabelsInstruction {


    public PrintInstruction(ExprInstruction expr, int numOfMsg) {
        super(expr,numOfMsg);
        this.nameOfLabel = getType("p_print_",expr);
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        String reg =expr.getLocationString();
        out.println("MOV r0, " + reg);
        out.println("BL " + nameOfLabel);
    }

    public int addDataAndLabels() {
        if (!type.checkType(PrimType.CHAR)) {
            if (type.checkType(PrimType.INT)) {
                String nameOfMsg = setData("\"%d\\0\"");
                String[] namesOfMsg = {nameOfMsg};
                setLabel(nameOfLabel, namesOfMsg);
            } else {
                String nameOfMsg1;
                String nameOfMsg2;

                if (type.checkType(PrimType.STRING)) {
                    nameOfMsg1 = setData(((StringLiterInstruction) expr).getStringLiter());
                    nameOfMsg2 = setData("\"%.*s\\0\"");

                } else {
                    nameOfMsg1 = setData("\"true\\0\"");
                    nameOfMsg2 = setData("\"false\\0\"");
                }
                String[] namesOfMsg = {nameOfMsg1, nameOfMsg2};
                setLabel(nameOfLabel, namesOfMsg);
            }
        }
        return numOfMsg;
    }

}
