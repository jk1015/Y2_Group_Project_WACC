package wacc.instructions;

import wacc.LabelMaker;

import java.io.PrintStream;

/**
 * Created by jaspreet on 08/12/16.
 */
public class ForInstruction implements Instruction {

    private InitAssignInstruction counterInit;
    private LocatableInstruction idExpr;
    private LocatableInstruction endExpr;
    private Instruction stat;
    private int scopeSize;
    private AssignInstruction incrementId;
    private LabelMaker labelMaker;

    public ForInstruction(InitAssignInstruction counterInit, LocatableInstruction idExpr,
                          LocatableInstruction endExpr, Instruction stat, AssignInstruction incrementId, int scopeSize) {
        this.counterInit = counterInit;
        this.idExpr = idExpr;
        this.endExpr = endExpr;
        this.stat = stat;
        this.scopeSize = scopeSize;
        this.incrementId = incrementId;
        this.labelMaker = LabelMaker.getLabelMaker();
    }

    @Override
    public void toAssembly(PrintStream out) {
        counterInit.toAssembly(out);
        out.println();
        out.println("B " + labelMaker.getLabel(this, 0));
        out.println(labelMaker.getLabel(this, 1) + ":");
        stat.toAssembly(out);
        if (scopeSize > 0) {
            out.println("ADD sp, sp, #" + scopeSize);
        }
        out.println(labelMaker.getLabel(this, 0) + ":");
        idExpr.toAssembly(out);
        endExpr.toAssembly(out);
        out.println("CMP " + idExpr.getLocationString() + ", " + endExpr.getLocationString());
        out.println("BEQ " + labelMaker.getLabel(this, 2));
        incrementId.toAssembly(out);
        out.println("B " + labelMaker.getLabel(this, 1));
        out.println(labelMaker.getLabel(this, 2) + ':');
    }
}
