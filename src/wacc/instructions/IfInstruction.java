package wacc.instructions;

import wacc.LabelMaker;
import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;
import java.util.List;

public class IfInstruction implements Instruction {

    final private List<ExprInstruction> exprs;
    final private LabelMaker labelMaker;
    final private List<Instruction> stats;
    final private List<Integer> scopeSizes;

    public IfInstruction(List<ExprInstruction> exprs, List<Instruction> stats, List<Integer> scopeSizes) {
        this.exprs = exprs;
        this.stats = stats;
        this.scopeSizes = scopeSizes;
        labelMaker = LabelMaker.getLabelMaker();
    }

    @Override
    public void toAssembly(PrintStream out) {
        for (int i = 0; i < stats.size(); i++) {
            if (i < exprs.size()) {
                ExprInstruction expr = exprs.get(i);
                expr.toAssembly(out);
                out.println("CMP " + expr.getLocationString() + ", #0");
                out.println("BEQ " + labelMaker.getLabel(this, i));
            }
            stats.get(i).toAssembly(out);
            int scopeSize = scopeSizes.get(i);
            if (scopeSize > 0) {
                out.println("ADD sp, sp, #" + scopeSize);
            }
            if (i < stats.size() - 1) {
                out.println("B " + labelMaker.getLabel(this, stats.size() - 1));
            }
            out.println(labelMaker.getLabel(this, i) + ":");
        }
    }

    /*
    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("CMP " + expr.getLocationString() + ", #0");
        out.println("BEQ " + labelMaker.getLabel(this, 0));
        stat1.toAssembly(out);
        if (scopeSize1 > 0) {
            out.println("ADD sp, sp, #" + scopeSize1);
        }
        out.println("B " + labelMaker.getLabel(this, 1));
        out.println(labelMaker.getLabel(this, 0) + ":");
        stat2.toAssembly(out);
        if (scopeSize2 > 0) {
            out.println("ADD sp, sp, #" + scopeSize2);
        }
        out.println(labelMaker.getLabel(this, 1) + ":");
    }
    */

}
