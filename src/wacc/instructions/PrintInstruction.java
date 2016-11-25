package wacc.instructions;


import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.ArrayType;
import wacc.types.PrimType;
import wacc.types.Type;

import java.io.PrintStream;

public class PrintInstruction extends ContainingDataOrLabelsInstruction {


    public PrintInstruction(ExprInstruction expr, int numOfMsg) {
        super(expr,numOfMsg);
        this.nameOfLabel = getType("p_print_",expr);
    }

    @Override
    public void toAssembly(PrintStream out) {
        expr.toAssembly(out);
        String reg =expr.getLocationString();
        out.println("MOV r0, " + reg);
        out.println("BL " + nameOfLabel);
    }

    public int addDataAndLabels() {
        String prefix = "msg_";
        if (!type.checkType(PrimType.CHAR)) {
            if (type.checkType(PrimType.INT)) {
                String nameOfMsg = setData(prefix + numOfMsg,"\"%d\\0\"");
                numOfMsg++;
                String[] namesOfMsg = {nameOfMsg};
                setLabel(nameOfLabel, namesOfMsg);
            } else if (type.checkType(PrimType.STRING) ||
                    type.checkType(PrimType.BOOL)){
                String nameOfMsg1;
                String nameOfMsg2;

                if (type.checkType(PrimType.STRING)) {
                    nameOfMsg1 = setData(prefix + numOfMsg,((StringLiterInstruction) expr).getStringLiter());
                    numOfMsg++;
                    nameOfMsg2 = setData(prefix + numOfMsg,"\"%.*s\\0\"");
                    numOfMsg++;

                } else {
                    nameOfMsg1 = setData(prefix + numOfMsg,"\"true\\0\"");
                    numOfMsg++;
                    nameOfMsg2 = setData(prefix + numOfMsg,"\"false\\0\"");
                    numOfMsg++;
                }
                String[] namesOfMsg = {nameOfMsg1, nameOfMsg2};
                setLabel(nameOfLabel, namesOfMsg);
            }else {
                String nameOfMsg = setData(prefix + numOfMsg,"\"%p\\0\"");
                numOfMsg++;
                String[] namesOfMsg = {nameOfMsg};
                setLabel(nameOfLabel, namesOfMsg);
            }
        }
        return numOfMsg;
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

}


