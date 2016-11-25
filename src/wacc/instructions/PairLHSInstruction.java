package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PairType;
import wacc.types.Type;

import java.io.PrintStream;

public class PairLHSInstruction extends CanThrowRuntimeError implements LocatableInstruction {

    private final boolean isTokenFST;
    private final ExprInstruction expr;

    public PairLHSInstruction(boolean isTokenFST, ExprInstruction expr, int numOfMsg) {
        super(numOfMsg);
        this.isTokenFST = isTokenFST;
        this.expr = expr;
    }

    @Override
    public String getLocationString() {
        return "[" + expr.getLocationString() + "]";
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

    @Override
    public int setErrorChecking() {
        String[] ascii = {"NullReferenceError: dereference a null reference\n\0"};
        numOfMsg = addDataAndLabels("BL p_check_null_pointer", ascii);

        numOfMsg = addDataAndLabels("p_throw_runtime_error", ascii);
        String[] stringAscii = {"\"%.*s\\0\""};
        numOfMsg = addDataAndLabels("p_print_string", stringAscii);
        return numOfMsg;
    }
}
