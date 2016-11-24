package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class InitAssignInstruction implements Instruction{

    private ExprInstruction expr;
    private String var;

    public InitAssignInstruction(ExprInstruction expr, String var) {
        this.expr = expr;
        this.var = var;
    }


    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("SUB sp, sp, #4");
        out.println("STR " + expr.getLocationString() + ", " + var);
    }
}
