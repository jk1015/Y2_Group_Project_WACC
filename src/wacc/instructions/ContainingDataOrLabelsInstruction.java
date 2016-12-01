package wacc.instructions;

import java.util.ArrayList;
import java.util.HashMap;
public  class ContainingDataOrLabelsInstruction {
    private HashMap<String,String> dataMap;
    private ArrayList<LabelInstruction> labels = new ArrayList<>();
    private ArrayList<DataInstruction> data = new ArrayList<>();
    private int numOfMsg;

    public ContainingDataOrLabelsInstruction(HashMap<String, String> dataMap){
        this.dataMap = dataMap;
        this.numOfMsg = dataMap.size();
    }
    public ContainingDataOrLabelsInstruction() {
    }

    public ArrayList<DataInstruction> getData() {
        return data;
    }

    public ArrayList<LabelInstruction> getLabel() {
        return labels;
    }

    protected void addData(DataInstruction newData){
        data.add(newData);
    }

    protected void addLabel(LabelInstruction newLabel){

        labels.add(newLabel);
    }

    public void setLabel(String label, String[] namesOfMsg) {
        LabelInstruction labelInstruction = new LabelInstruction(label, namesOfMsg);
        addLabel(labelInstruction);
    }

    public void setData(String ascii,String name) {
        DataInstruction dataInstruction = new DataInstruction(name, ascii);
        addData(dataInstruction);
        dataMap.put(ascii,name);
    }

    public HashMap<String,String> addDataAndLabels(String name, String[] ascii) {
        String[] namesOfMsg = new String[ascii.length];
        String prefix = "msg_";
        String nameOfMsg;
        for (int i = 0; i < ascii.length; i++) {
            if (!dataMap.containsKey(ascii[i])) {
                nameOfMsg = prefix + numOfMsg;
                setData(ascii[i],nameOfMsg);
                numOfMsg++;
            } else {
                nameOfMsg = dataMap.get(ascii[i]);
            }
            namesOfMsg[i] = nameOfMsg;
        }
        setLabel(name, namesOfMsg);
        return dataMap;
    }

}
