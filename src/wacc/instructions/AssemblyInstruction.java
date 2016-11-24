package wacc.instructions;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by jk1015 on 23/11/16.
 */
public class AssemblyInstruction implements Instruction {

    private List<DataInstruction> data;
    private ProgramInstruction program;
    private List<LabelInstruction> labels;

    public AssemblyInstruction(List<DataInstruction> data, ProgramInstruction program, List<LabelInstruction> labels) {
        this.data = data;
        this.program = program;
        this.labels = labels;
    }


    @Override
    public void toAssembly(PrintStream out) {
        if (data != null){
            out.println(".data");
        }
        for (DataInstruction d : data){
            d.toAssembly(out);
        }
        program.toAssembly(out);
        for (LabelInstruction l : labels){
            l.toAssembly(out);
        }
    }
}
