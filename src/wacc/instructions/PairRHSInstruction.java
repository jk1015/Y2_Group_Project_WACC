package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PairType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.HashMap;

public class PairRHSInstruction implements LocatableInstruction {

    private final boolean isTokenFST;
    private final ExprInstruction expr;
    private HashMap<String, String> dataMap;
    private ContainingDataOrLabelsInstruction dataAndLabels;

    public PairRHSInstruction(boolean isTokenFST, ExprInstruction expr, HashMap<String, String> dataMap) {
        this.isTokenFST = isTokenFST;
        this.expr = expr;
        this.dataMap = dataMap;
        this.dataAndLabels = new ContainingDataOrLabelsInstruction(dataMap);
    }

    @Override
    public void toAssembly(PrintStream out) {
        String exprLocation = expr.getLocationString();
        expr.toAssembly(out);
        out.println("MOV r0, " + exprLocation);
        out.println("BL p_check_null_pointer");
        if (isTokenFST) {
            out.println("LDR " + exprLocation + ", [" + exprLocation + "]");
        } else {
            out.println("LDR " + exprLocation + ", [" + exprLocation + ", #4]");
        }
        out.println("LDR " + exprLocation + ", [" + exprLocation + "]");
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
    public boolean usesRegister() {
        return true;
    }

    public HashMap<String, String> setErrorChecking() {
        String[] ascii = {"\"NullReferenceError: dereference a null reference\\n\\0\""};
        dataMap = dataAndLabels.addDataAndLabels("p_check_null_pointer", ascii);
        dataMap = dataAndLabels.addDataAndLabels("p_throw_runtime_error", ascii);
        String[] stringAscii = {"\"%.*s\\0\""};
        dataMap = dataAndLabels.addDataAndLabels("p_print_string", stringAscii);
        return dataMap;
    }

    public ContainingDataOrLabelsInstruction getDataAndLabels(){
        return dataAndLabels;
    }
}
