package wacc.instructions.locatable.expressions.unaryExpressions;

import wacc.instructions.statement.ContainingDataOrLabelsInstruction;
import wacc.instructions.locatable.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Created by jk1015 on 22/11/16.
 */
public class NegInstruction extends ExprInstruction {

    private HashMap<String, String> dataMap;
    private ExprInstruction expr;
    private ContainingDataOrLabelsInstruction dataAndLabels;

    public NegInstruction(ExprInstruction expr, int register,HashMap<String,String> dataMap) {
        super(register, PrimType.INT);
        this.expr = expr;
        this.dataMap = dataMap;
        this.dataAndLabels = new ContainingDataOrLabelsInstruction(dataMap);
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("RSBS " + getLocationString() + ", " + expr.getLocationString() + ", #0");
        out.println("BLVS p_throw_overflow_error");
    }

    public HashMap<String, String> setCheckError() {
        String[] overflow =
                {"\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\""};
        String[] stringASCII = {"\"%.*s\\0\""};
        dataMap = dataAndLabels.addDataAndLabels("p_throw_overflow_error", overflow);
        dataMap = dataAndLabels.addDataAndLabels("p_throw_runtime_error", overflow);
        dataMap = dataAndLabels.addDataAndLabels("p_print_string", stringASCII);
        return dataMap;
    }
    public ContainingDataOrLabelsInstruction getDataAndLabels(){
        return dataAndLabels;
    }
}
