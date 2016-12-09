package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PairType;

import java.io.PrintStream;
import java.util.HashMap;

public class FreeInstruction implements Instruction {
    protected HashMap<String, String> dataMap;
    protected ContainingDataOrLabelsInstruction errorPrint;
    ExprInstruction expr;
    String name;
    String ascii;

    public FreeInstruction(ExprInstruction expr, HashMap<String, String> dataMap) {
        this.expr = expr;
        this.dataMap = dataMap;
        this.errorPrint = new ContainingDataOrLabelsInstruction(dataMap);
        if (expr.getType() instanceof PairType) {
            name = "p_free_pair";
        } else {
            name = "p_free_array";
        }
        ascii = "\"NullReferenceError: dereference a null reference\\n\\0\"";
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("MOV r0, " + expr.getLocationString());
        out.println("BL " + name);
    }

    public HashMap<String,String> setCheckError() {
        dataMap = addDataAndLabels(name,
                ascii);
        dataMap = addDataAndLabels("p_throw_runtime_error",
                ascii);
        dataMap = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        return dataMap;
    }

    public ContainingDataOrLabelsInstruction getErrorPrint(){
        return errorPrint;
    }

    protected HashMap<String,String> addDataAndLabels(String name, String ascii) {
        String[] asciis = {ascii};
        dataMap = errorPrint.addDataAndLabels(name,asciis);
        return dataMap;
    }
}
