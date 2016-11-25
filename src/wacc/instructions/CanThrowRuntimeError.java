package wacc.instructions;

import wacc.instructions.expressions.baseExpressions.StringLiterInstruction;
import wacc.types.PrimType;

public abstract class CanThrowRuntimeError extends ContainingDataOrLabelsInstruction {

    protected CanThrowRuntimeError(int numOfMsg){
        this.numOfMsg = numOfMsg;
    }

    public abstract int setErrorChecking();

    protected int addDataAndLabels(String name, String[] ascii) {
        String prefix = "msg_";
        String[] namesOfMsg = new String[ascii.length];
        for (int i = 0; i<ascii.length; i++){
            String nameOfMsg = setData(prefix + numOfMsg, ascii[0]);
            namesOfMsg[i] = nameOfMsg;
            numOfMsg++;
        }
        setLabel(name, namesOfMsg);
        return numOfMsg;
    }

}
