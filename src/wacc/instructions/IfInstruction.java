package wacc.instructions;

import wacc.LabelMaker;

import java.io.PrintStream;

public class IfInstruction implements Instruction {

    final private ExprInstruction expr;
    final private LabelMaker labelMaker;
    final private Instruction stat1;
    final private Instruction stat2;

    public IfInstruction(ExprInstruction expr, Instruction stat1, Instruction stat2) {
        this.expr = expr;
        this.stat1 = stat1;
        this.stat2 = stat2;
        labelMaker = LabelMaker.getLabelMaker();
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("CMP " + expr.getLocationString() + ", #0");
        out.println("BEQ " + labelMaker.getLabel(this, 0));
        stat1.toAssembly(out);
        out.println("B " + labelMaker.getLabel(this, 1));
        out.println(labelMaker.getLabel(this, 0) + ":");
        stat2.toAssembly(out);
        out.println(labelMaker.getLabel(this, 1) + ":");
    }
}
