package wacc.instructions;

import antlr.WACCParser;
import wacc.LabelMaker;
import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;

public class IfInstruction implements Instruction {

    final private ExprInstruction expr;
    final private LabelMaker labelMaker;
    final private Instruction stat1;
    final private Instruction stat2;
    final private int scopeSize1;
    final private int scopeSize2;
    private final int ctx;

    public IfInstruction(ExprInstruction expr, Instruction stat1, Instruction stat2,
                         int scopeSize1, int scopeSize2, int ctx) {
        this.expr = expr;
        this.stat1 = stat1;
        this.stat2 = stat2;
        this.scopeSize1 = scopeSize1;
        this.scopeSize2 = scopeSize2;
        labelMaker = LabelMaker.getLabelMaker();
        this.ctx = ctx;
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("CMP " + expr.getLocationString() + ", #0");
        out.println("BEQ " + labelMaker.getLabel(ctx, 0));
        stat1.toAssembly(out);
        if (scopeSize1 > 0) {
            out.println("ADD sp, sp, #" + scopeSize1);
        }
        out.println("B " + labelMaker.getLabel(ctx, 1));
        out.println(labelMaker.getLabel(ctx, 0) + ":");
        stat2.toAssembly(out);
        if (scopeSize2 > 0) {
            out.println("ADD sp, sp, #" + scopeSize2);
        }
        out.println(labelMaker.getLabel(ctx, 1) + ":");
    }
}
