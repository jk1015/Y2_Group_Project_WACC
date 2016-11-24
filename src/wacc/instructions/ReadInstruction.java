package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class ReadInstruction extends ContainingDataOrLabelsInstruction {
    AssignLHSInstruction lhsInstruction;

    public ReadInstruction(AssignLHSInstruction assignLHSInstruction) {
        super();
        this.lhsInstruction = assignLHSInstruction;
    }

    @Override
    public void toAssembly(PrintStream out) {

        ExprInstruction expr = lhsInstruction.getExpr();
        out.println("ADD " + lhsInstruction.getLocationString() + ", sp, #0");
        out.println("MOV " + " r0," + lhsInstruction.getLocationString());
        out.println("BL " + getType("p_read",expr));
        out.println("ADD "  + "sp, sp, #4");
    }

    @Override
    protected int addDataAndLabels() {
        return 0;
    }
}
