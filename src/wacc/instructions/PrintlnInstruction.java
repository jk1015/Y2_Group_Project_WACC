package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class PrintlnInstruction extends ContainingDataOrLabelsInstruction {
    private ExprInstruction expr;
    private PrintInstruction printInstruction;

    public PrintlnInstruction(ExprInstruction expr) {
        this.expr = expr;
        printInstruction = new PrintInstruction(expr,true);
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        printInstruction.toAssembly(out);
    }
}
