package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PairType;
import wacc.types.Type;

import java.io.PrintStream;

public class PairRHSInstruction implements LocatableInstruction {

    private final boolean isTokenFST;
    private final ExprInstruction expr;
    private CanThrowRuntimeError canThrowRuntimeError;
    private int numOfMsg;

    public PairRHSInstruction(boolean isTokenFST, ExprInstruction expr, int numOfMsg) {
        this.isTokenFST = isTokenFST;
        this.expr = expr;
        this.numOfMsg = numOfMsg;
        this.canThrowRuntimeError = new CanThrowRuntimeError(numOfMsg);
    }

    @Override
    public void toAssembly(PrintStream out) {
        String exprLocation = expr.getLocationString();
        expr.toAssembly(out);
        out.println("MOV r0, " + exprLocation);
        out.println("BL p_check_null_pointer");
        out.println("LDR " + exprLocation + ", [" + exprLocation + "]");
        if (isTokenFST) {
            out.println("LDR " + exprLocation + ", [" + exprLocation + "]");
        } else {
            out.println("LDR " + exprLocation + ", [" + exprLocation + ", #4]");
        }
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

    public int setErrorChecking() {
        String[] ascii = {"NullReferenceError: dereference a null reference\n\0"};
        numOfMsg = canThrowRuntimeError.addDataAndLabels("BL p_check_null_pointer", ascii);

        numOfMsg = canThrowRuntimeError.addDataAndLabels("p_throw_runtime_error", ascii);
        String[] stringAscii = {"\"%.*s\\0\""};
        numOfMsg = canThrowRuntimeError.addDataAndLabels("p_print_string", stringAscii);
        return numOfMsg;
    }

    public CanThrowRuntimeError getCanThrowRuntimeError(){
        return canThrowRuntimeError;
    }
}
