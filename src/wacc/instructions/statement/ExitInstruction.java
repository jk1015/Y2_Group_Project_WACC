package wacc.instructions.statement;

import wacc.instructions.Instruction;
import wacc.instructions.locatable.expressions.ExprInstruction;

import java.io.PrintStream;

public class ExitInstruction implements Instruction {

    private final ExprInstruction expr;

    public ExitInstruction(ExprInstruction expr) {
        this.expr = expr;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("MOV r0, " + expr.getLocationString());
        out.println("BL exit");
    }
}
