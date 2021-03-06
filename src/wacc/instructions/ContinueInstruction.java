package wacc.instructions;

import antlr.WACCParser;
import wacc.LabelMaker;

import java.io.PrintStream;

/**
 * Created by ad5115 on 05/12/16.
 */
public class ContinueInstruction implements Instruction {
    private final int ctx;
    private LabelMaker labelMaker;

    public ContinueInstruction(int ctx) {
        this.labelMaker = LabelMaker.getLabelMaker();
        this.ctx = ctx;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("B " + labelMaker.getLabel(ctx, 1));
    }
}
