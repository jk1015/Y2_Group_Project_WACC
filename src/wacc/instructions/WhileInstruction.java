package wacc.instructions;

import antlr.WACCParser;
import wacc.LabelMaker;
import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class WhileInstruction implements Instruction {

    final private ExprInstruction expr;
    final private LabelMaker labelMaker;
    final private Instruction stat;
    final private int scopeSize;
    private final int ctx;

    public WhileInstruction(ExprInstruction expr, Instruction stat, int scopeSize, int ctx) {
        this.expr = expr;
        this.stat = stat;
        this.scopeSize = scopeSize;
        labelMaker = LabelMaker.getLabelMaker();
        this.ctx = ctx;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("B " + labelMaker.getLabel(ctx, 0));
        out.println(labelMaker.getLabel(ctx, 1) + ":");
        stat.toAssembly(out);
        if (scopeSize > 0) {
            out.println("ADD sp, sp, #" + scopeSize);
        }
        out.println(labelMaker.getLabel(ctx, 0) + ":");
        expr.toAssembly(out);
        out.println("CMP " + expr.getLocationString() + ", #1");
        out.println("BEQ " + labelMaker.getLabel(ctx, 1));
        out.println(labelMaker.getLabel(ctx, 3) + ":");
    }
}
