package wacc.instructions.locatable.assignment;

import wacc.instructions.locatable.LocatableInstruction;
import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.PairType;
import wacc.types.Type;

import java.io.PrintStream;

public class NewPairInstruction implements LocatableInstruction {

    private final int currentReg;
    private final ExprInstruction exprA;
    private final ExprInstruction exprB;

    public NewPairInstruction(int currentReg, ExprInstruction exprA, ExprInstruction exprB) {
        this.currentReg = currentReg;
        this.exprA = exprA;
        this.exprB = exprB;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("LDR r0, =8");
        out.println("BL malloc");
        out.println("MOV r" + currentReg + ", r0");
        exprA.toAssembly(out);
        out.println("LDR r0, =4");
        out.println("BL malloc");
        out.println("STR " + exprA.getLocationString() + ", [r0]");
        out.println("STR r0, [r" + currentReg + "]");
        exprB.toAssembly(out);
        out.println("LDR r0, =4");
        out.println("BL malloc");
        out.println("STR " + exprB.getLocationString() + ", [r0]");
        out.println("STR r0, [r" + currentReg + ", #4]");
    }

    @Override
    public String getLocationString() {
        return "r"+currentReg;
    }

    @Override
    public Type getType() {
        return new PairType(exprA.getType(), exprB.getType());
    }
}
