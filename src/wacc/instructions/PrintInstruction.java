package wacc.instructions;


import java.io.PrintStream;

public class PrintInstruction extends ContainingDataOrLabelsInstruction {

    private ExprInstruction expr;
    private boolean println;

    public PrintInstruction(ExprInstruction expr, boolean println) {
        this.expr = expr;
        this.println = println;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        if (println){
            out.println("BL p_print_ln");
        } else {
            out.println("BL " + getType("p_print_",expr));
        }
    }

}
