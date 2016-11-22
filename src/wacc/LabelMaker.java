package wacc;

import wacc.instructions.Instruction;

import java.util.HashMap;
import java.util.Map;

public class LabelMaker {
    private final Map<Instruction, String> labels;
    private static LabelMaker labelMaker;

    private LabelMaker() {
        labels = new HashMap<Instruction, String>();
    }

    public static LabelMaker getLabelMaker() {
        if (labelMaker == null) {
            labelMaker = new LabelMaker();
        }
        return labelMaker;
    }

    public String getLabel(Instruction ins, int no) {
        String label = labels.get(ins);
        if (label == null) {
            label = "L" + labels.size() + "N";
            labels.put(ins, label);
        }
        return label + no;
    }
}
