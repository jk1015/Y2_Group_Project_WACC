package wacc.instructions;

import wacc.LabelMaker;

import java.io.PrintStream;

public class WhileInstruction implements Instruction {

    final private ExprInstruction expr;
    final private LabelMaker labelMaker;
    final private Instruction stat;

    public WhileInstruction(ExprInstruction expr, Instruction stat) {
        this.expr = expr;
        this.stat = stat;
        labelMaker = LabelMaker.getLabelMaker();
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("B " + labelMaker.getLabel(this, 0));
        out.println(labelMaker.getLabel(this, 1) + ":");
        stat.toAssembly(out);
        out.println(labelMaker.getLabel(this, 0) + ":");
        expr.toAssembly(out);
        out.println("CMP " + expr.getLocationString() + ", #1");
        out.println("BEQ " + labelMaker.getLabel(this, 1));
    }
}
