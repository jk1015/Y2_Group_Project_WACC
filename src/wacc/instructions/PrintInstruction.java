package wacc.instructions;
import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.IdentifierExprInstruction;
import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.PrimType;
import wacc.types.Type;
import java.io.PrintStream;
import java.util.HashMap;

public class PrintInstruction implements Instruction {

    private final ExprInstruction expr;
    private final Type type;
    private final String nameOfLabel;
    protected HashMap<String, String> dataMap;
    protected ContainingDataOrLabelsInstruction dataAndLabels;

    public PrintInstruction(ExprInstruction expr, HashMap<String,String> dataMap) {
        this.expr = expr;
        this.dataMap = dataMap;
        this.nameOfLabel = getType("p_print_",expr);
        this.type = expr.getType();
        this.dataAndLabels = new ContainingDataOrLabelsInstruction(dataMap);
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        String reg =expr.getLocationString();
        out.println("MOV r0, " + reg);
        out.println("BL " + nameOfLabel);
    }

    public HashMap<String,String> addDataAndLabels() {
        if (type.checkType(PrimType.CHAR)) {

        } else if (type.checkType(PrimType.INT)) {
            String[] ascii = {"\"%d\\0\""};
            dataMap = dataAndLabels.addDataAndLabels(nameOfLabel,ascii);
        } else if (type.checkType(PrimType.STRING) ||
                type.checkType(PrimType.BOOL)){
            String[] ascii = new String[2];
            if (type.checkType(PrimType.STRING)) {
                String ascii0;
                if (expr instanceof IdentifierExprInstruction){
                    ascii0 = ((IdentifierExprInstruction) expr).getStringValue();
                }else {
                    ascii0 = ((StringLiterInstruction) expr).getStringLiter();
                }
                ascii[0] = ascii0;
                ascii[1] = "\"%.*s\\0\"";
                dataMap = dataAndLabels.addDataAndLabels(nameOfLabel,ascii);
            } else {
                ascii[0] = "\"true\\0\"";
                ascii[1] = "\"false\\0\"";
                dataMap = dataAndLabels.addDataAndLabels(nameOfLabel,ascii);
            }

        }else {
            String[] ascii = {"\"%p\\0\""};
            dataMap = dataAndLabels.addDataAndLabels(nameOfLabel,ascii);
        }
        return dataMap;
    }

    protected String getType(String string, ExprInstruction exprIns) {
        Type type = exprIns.getType();

        if (type.checkType(PrimType.CHAR)) {
            return "putchar";
        } else if (type.checkType(PrimType.INT)) {
                return string + "int";
        } else if (type.checkType(PrimType.BOOL)) {
                return string + "bool";
        } else if (type.checkType(PrimType.STRING)){
                return string + "string";
        }else {
            return "p_print_reference";
        }

    }

    public ContainingDataOrLabelsInstruction getDataAndLabels(){
        return dataAndLabels;
    }

}


