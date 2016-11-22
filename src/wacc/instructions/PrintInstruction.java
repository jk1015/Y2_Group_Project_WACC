package wacc.instructions;

import java.io.PrintStream;

public class PrintInstruction extends ContainingDataOrLabelsInstruction {

    private ExprInstruction expr;

    public PrintInstruction(ExprInstruction expr) {
        this.expr = expr;
    }

    @Override
    public void toAssembly(PrintStream out) {

    }
}
