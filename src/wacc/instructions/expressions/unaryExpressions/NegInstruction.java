package wacc.instructions.expressions.unaryExpressions;

import wacc.instructions.ContainingDataOrLabelsInstruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class NegInstruction extends ExprInstruction {

    private ExprInstruction expr;
    ContainingDataOrLabelsInstruction dataAndLabels;
    private int numOfMsg;

    public NegInstruction(ExprInstruction expr, int register,int numOfMsg) {
        super(register, PrimType.INT);
        this.expr = expr;
        this.numOfMsg =numOfMsg;
        this.dataAndLabels = new ContainingDataOrLabelsInstruction(numOfMsg);
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        out.println("RSBS " + getLocationString() + ", " + expr.getLocationString() + ", #0");
        out.println("BLVS p_throw_overflow_error");
    }

    public int setDataAndLAbels(){
        String nameOfMsg = dataAndLabels.setData("msg_" + numOfMsg,"\"%d\\0\"");
        numOfMsg++;
        String[] namesOfMsg = {nameOfMsg};
        dataAndLabels.setLabel("p_throw_overflow_error", namesOfMsg);
        return numOfMsg;
    }

    public int setCheckError() {
        numOfMsg = addDataAndLabels("p_throw_overflow_error",
                "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
        numOfMsg = addDataAndLabels("p_throw_runtime_error",
                "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
        numOfMsg = addDataAndLabels("p_print_string", "\"%.*s\\0\"");
        return numOfMsg;
    }

    private int addDataAndLabels(String name, String ascii) {
        String prefix = "msg_";
        String nameOfMsg = dataAndLabels.setData(prefix + numOfMsg, ascii);
        numOfMsg++;
        String[] namesOfMsg = {nameOfMsg};
        dataAndLabels.setLabel(name, namesOfMsg);
        return numOfMsg;
    }

    public ContainingDataOrLabelsInstruction getDataAndLabels(){
        return dataAndLabels;
    }
}
