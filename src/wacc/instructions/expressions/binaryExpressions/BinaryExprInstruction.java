package wacc.instructions.expressions.binaryExpressions;

import wacc.instructions.ContainingDataOrLabelsInstruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public abstract class BinaryExprInstruction extends ExprInstruction {
    protected ContainingDataOrLabelsInstruction errorPrint;
    private ExprInstruction expr1, expr2;
    protected int numOfMsg;

    public BinaryExprInstruction(ExprInstruction expr1, ExprInstruction expr2, int register, Type type, int numOfMsg) {
        super(register, type);
        this.expr1 = expr1;
        this.expr2 = expr2;
        errorPrint = new ContainingDataOrLabelsInstruction();
        this.numOfMsg = numOfMsg;
    }

    public BinaryExprInstruction(ExprInstruction expr1, ExprInstruction expr2, int register, Type type) {
        super(register, type);
        this.expr1 = expr1;
        this.expr2 = expr2;
        errorPrint = new ContainingDataOrLabelsInstruction();
    }

    public void toAssembly(PrintStream out) {
        expr1.toAssembly(out);
        if(expr1.getLocationString().equals("r10")) {
            out.println("PUSH {r10}");
        }
        expr2.toAssembly(out);
        if(expr1.getLocationString().equals("r10")) {
            out.println("POP {r11}");
            expr1.setRegister(11);
        }
    }

    public abstract int setCheckError();

    public String getExpr1String() {
        return expr1.getLocationString();
    }

    public String getExpr2String() {
        return expr2.getLocationString();
    }

    public ContainingDataOrLabelsInstruction getErrorPrint(){
        return errorPrint;
    }

    protected int addDataAndLabels(String name, String ascii) {
        String prefix = "msg_";
        String nameOfMsg = errorPrint.setData(prefix + numOfMsg, ascii);
        numOfMsg++;
        String[] namesOfMsg = {nameOfMsg};
        errorPrint.setLabel(name, namesOfMsg);
        return numOfMsg;
    }

}
