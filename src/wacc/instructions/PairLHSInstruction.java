package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PairType;
import wacc.types.Type;

import java.io.PrintStream;

public class PairLHSInstruction implements LocatableInstruction {

    private final boolean isTokenFST;
    private final ExprInstruction expr;

    public PairLHSInstruction(boolean isTokenFST, ExprInstruction expr) {
        this.isTokenFST = isTokenFST;
        this.expr = expr;
    }

    @Override
    public String getLocationString() {
        return expr.getLocationString();
    }

    @Override
    public Type getType() {
        if (isTokenFST) {
            return ((PairType) expr.getType()).getType1();
        } else {
            return ((PairType) expr.getType()).getType2();
        }
    }

    @Override
    public void toAssembly(PrintStream out) {
        String exprLocation = expr.getLocationString();
        out.println(expr);
        out.println("MOV r0, " + exprLocation);
        out.println("BL p_check_null_pointer");
        out.println("LDR " + exprLocation + ", [" + exprLocation + "]");
        if (!isTokenFST) {
            out.println("ADD " + exprLocation + ", " + exprLocation + ", #4");
        }
    }
}
