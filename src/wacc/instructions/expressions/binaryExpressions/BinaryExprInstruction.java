package wacc.instructions.expressions.binaryExpressions;

import wacc.instructions.ContainingDataOrLabelsInstruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.FloatLiterInstruction;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Created by jk1015 on 22/11/16.
 */
public abstract class BinaryExprInstruction extends ExprInstruction {
    protected ContainingDataOrLabelsInstruction errorPrint;

    protected ExprInstruction expr1, expr2;
    protected HashMap<String,String>  dataMap;
    protected float floatValue;


    public BinaryExprInstruction(ExprInstruction expr1, ExprInstruction expr2, int register,
                                 Type type, HashMap<String,String> dataMap) {
        super(register, type);
        if ((expr1 instanceof FloatLiterInstruction ||
                expr2 instanceof FloatLiterInstruction)){
            this.type = PrimType.FLOAT;
        }
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.dataMap = dataMap;
        this.errorPrint = new ContainingDataOrLabelsInstruction(dataMap);
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

    public abstract HashMap<String,String>  setCheckError();

    public String getExpr1String() {
        return expr1.getLocationString();
    }

    public String getExpr2String() {
        return expr2.getLocationString();
    }

    public ContainingDataOrLabelsInstruction getErrorPrint(){
        return errorPrint;
    }

    protected HashMap<String,String> addDataAndLabels(String name, String ascii) {
        String[] asciis = {ascii};
        dataMap = errorPrint.addDataAndLabels(name,asciis);
        return dataMap;
    }


    public float getFloatValue(){
        return floatValue;
    }

}
