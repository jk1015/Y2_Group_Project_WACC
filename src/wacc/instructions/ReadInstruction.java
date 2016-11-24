package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

public class ReadInstruction extends ContainingDataOrLabelsInstruction {
    AssignLHSInstruction lhsInstruction;
    //ExprInstruction expr = lhsInstruction.getExpr();

    public ReadInstruction(AssignLHSInstruction assignLHSInstruction) {
        super();
        this.lhsInstruction = assignLHSInstruction;
        this.nameOfLabel = getType("p_read_",expr);
        //this.expr = lhsInstruction.getExpr();
    }

    @Override
    public void toAssembly(PrintStream out) {
        /*
        ExprInstruction expr = lhsInstruction.getExpr();
        out.println("ADD " + lhsInstruction.getLocationString() + ", sp, #0");
        out.println("MOV " + " r0," + lhsInstruction.getLocationString());
        out.println("BL " + getType("p_read",expr));
        out.println("ADD "  + "sp, sp, #4");
        */
    }

    @Override
    public int addDataAndLabels() {
        if (type.checkType(PrimType.CHAR)) {
            String nameOfMsg = setData("\"%c\\0\"");
            String[] namesOfMsg = {nameOfMsg};
            setLabel(nameOfLabel, namesOfMsg);
        }else if (type.checkType(PrimType.INT)) {
                String nameOfMsg = setData("\"%d\\0\"");
                String[] namesOfMsg = {nameOfMsg};
                setLabel(nameOfLabel, namesOfMsg);
        }
        return numOfMsg;
        }



}

