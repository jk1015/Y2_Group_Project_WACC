package wacc.instructions;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.Type;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ArrayElemLHSInstruction implements LocatableInstruction{
    private final String locationString;
    private final Type type;
    private final int currentReg;
    private final List<ExprInstruction> exprs;
    private HashMap<String, String> dataMap;
    private ContainingDataOrLabelsInstruction dataAndLabels;

    public ArrayElemLHSInstruction(String locationString, Type type,
                                   int currentReg, List<ExprInstruction> exprs, HashMap<String, String> dataMap) {
        this.locationString = locationString;
        this.type = type;
        this.currentReg = currentReg;
        this.exprs = exprs;
        this.dataMap = dataMap;
        this.dataAndLabels = new ContainingDataOrLabelsInstruction(dataMap);
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println("ADD r" + currentReg + ", sp, #" +  locationString);
        out.println("LDR r" + currentReg + ", [r" + currentReg + "]");
        Iterator<ExprInstruction> it = exprs.iterator();

        ExprInstruction current = it.next();

        current.toAssembly(out);
        out.println("MOV r0, " + current.getLocationString());
        out.println("MOV r1, r" + currentReg);
        out.println("BL p_check_array_bounds");
        out.println("ADD r" + currentReg + ", r" + currentReg + ", #4");
        out.println("ADD r" + currentReg + ", r" + currentReg + ", " + current.getLocationString() + ", LSL #2");

        while (it.hasNext() ) {
            out.println("LDR r" + currentReg + ", [r" + currentReg + "]");
            current = it.next();
            current.toAssembly(out);
            out.println("MOV r0, " + currentReg);
            out.println("MOV r1, r" + current.getLocationString());
            out.println("BL p_check_array_bounds");
            out.println("ADD r" + currentReg + ", r" + currentReg + ", #4");
            out.println("ADD r" + currentReg + ", r" + currentReg + ", " + current.getLocationString() + ", LSL #2");
        }

    }

    @Override
    public String getLocationString() {
        return "[r"+currentReg + "]";
    }

    @Override
    public Type getType() {
        return type;
    }

    public HashMap<String, String> setErrorChecking() {
        String[] ascii = {"\"ArrayIndexOutOfBoundsError: negative index\\n\\0\"",
                "\"ArrayIndexOutOfBoundsError: index too large\\n\\0\""};
        dataMap = dataAndLabels.addDataAndLabels("p_check_array_bounds", ascii);
        dataMap = dataAndLabels.addDataAndLabels("p_throw_runtime_error", ascii);
        String[] stringAscii = {"\"%.*s\\0\""};
        dataMap = dataAndLabels.addDataAndLabels("p_print_string", stringAscii);
        return dataMap;
    }

    public ContainingDataOrLabelsInstruction getDataAndLabels(){
        return dataAndLabels;
    }
}
