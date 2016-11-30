package wacc.instructions.locatable.assignment;

import wacc.instructions.locatable.LocatableInstruction;
import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.instructions.statement.ContainingDataOrLabelsInstruction;
import wacc.types.PairType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.HashMap;

public class PairLHSInstruction implements LocatableInstruction {

    private final boolean isTokenFST;
    private final ExprInstruction expr;
    private HashMap<String, String> dataMap;
    private ContainingDataOrLabelsInstruction dataAndLabels;

    public PairLHSInstruction(boolean isTokenFST, ExprInstruction expr, HashMap<String, String> dataMap) {
        this.isTokenFST = isTokenFST;
        this.expr = expr;
        this.dataMap = dataMap;
        this.dataAndLabels = new ContainingDataOrLabelsInstruction(dataMap);
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

    public HashMap<String, String> setErrorChecking() {
        String[] ascii = {"NullReferenceError: dereference a null reference\n\0"};
        dataMap = dataAndLabels.addDataAndLabels("BL p_check_null_pointer", ascii);
        dataMap = dataAndLabels.addDataAndLabels("p_throw_runtime_error", ascii);
        String[] stringAscii = {"\"%.*s\\0\""};
        dataMap = dataAndLabels.addDataAndLabels("p_print_string", stringAscii);
        return dataMap;
    }

    public ContainingDataOrLabelsInstruction getDataAndLabels(){
        return dataAndLabels;
    }
}
