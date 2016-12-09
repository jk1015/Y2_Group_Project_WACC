package wacc;

import antlr.WACCParser;
import wacc.instructions.Instruction;

import java.util.HashMap;
import java.util.Map;

public class LabelMaker {
    private final Map<Integer, String> labels;
    private static LabelMaker labelMaker;

    private LabelMaker() {
        labels = new HashMap<>();
    }

    public static LabelMaker getLabelMaker() {
        if (labelMaker == null) {
            labelMaker = new LabelMaker();
        }
        return labelMaker;
    }

    public static String getFunctionLabel(String functionIdentifier) {
        return "f_" + functionIdentifier;
    }

    public String getLabel(int ctx, int no) {
        String label = labels.get(ctx);
        if (label == null) {
            label = "L" + labels.size() + "N";
            labels.put(ctx, label);
        }
        return label + no;
    }
}
