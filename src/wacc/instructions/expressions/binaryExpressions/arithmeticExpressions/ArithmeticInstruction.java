package wacc.instructions.expressions.binaryExpressions.arithmeticExpressions;

import wacc.instructions.Instruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.FloatLiterInstruction;
import wacc.instructions.expressions.baseExpressions.IntLiterInstruction;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.HashMap;

public abstract class ArithmeticInstruction extends BinaryExprInstruction{

    protected Instruction ins;
    protected float f1;
    protected float f2;

    public ArithmeticInstruction(ExprInstruction expr1, ExprInstruction expr2, int register,
                                 Type type, HashMap<String,String> dataMap) {
        super(expr1, expr2, register, type, dataMap);
        if (expr1 instanceof FloatLiterInstruction ||
                expr2 instanceof FloatLiterInstruction) {
            if (expr1 instanceof FloatLiterInstruction &&
                    expr2 instanceof FloatLiterInstruction) {
                f1 = ((FloatLiterInstruction) expr1).getValueInFloat();
                f2 = ((FloatLiterInstruction) expr2).getValueInFloat();
            } else if (expr1 instanceof FloatLiterInstruction) {
                f1 = ((FloatLiterInstruction) expr1).getValueInFloat();
                f2 = (float) ((IntLiterInstruction) expr2).getValue();
            } else {
                f1 = (float) ((IntLiterInstruction) expr1).getValue();
                f2 = ((FloatLiterInstruction) expr2).getValueInFloat();
            }
            floatValue = operate(f1,f2);
            ins = new FloatLiterInstruction(this.dataMap.size(), register, floatValue);
        }else {
            ins = this;
        }
    }

    @Override
    public void toAssembly(PrintStream out) {
        super.toAssembly(out);
        if (expr1 instanceof FloatLiterInstruction ||
                expr2 instanceof FloatLiterInstruction) {
            ins.toAssembly(out);
        } else {
            assembly(out);
        }
    }

    protected abstract void assembly(PrintStream out);
    protected abstract float operate(float f1, float f2);
}
