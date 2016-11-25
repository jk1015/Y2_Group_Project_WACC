package wacc.instructions;


public class CanThrowRuntimeError extends ContainingDataOrLabelsInstruction {

    protected CanThrowRuntimeError(int numOfMsg){
        this.numOfMsg = numOfMsg;
    }


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
