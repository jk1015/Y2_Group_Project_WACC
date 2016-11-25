package wacc.instructions;

import wacc.instructions.statement.FunctionInstruction;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by jk1015 on 23/11/16.
 */
public class AssemblyInstruction implements Instruction {

    private List<DataInstruction> data;
    private ProgramInstruction program;
    private List<LabelInstruction> labels;
    private List<FunctionInstruction> funcs;

    public AssemblyInstruction(List<DataInstruction> data, List<FunctionInstruction> funcs, ProgramInstruction program, List<LabelInstruction> labels) {
        this.data = data;
        this.program = program;
        this.labels = labels;
        this.funcs = funcs;
    }


    @Override
    public void toAssembly(PrintStream out) {
        if (data != null){
            out.println(".data");
        }
        for (DataInstruction d : data){
            d.toAssembly(out);
        }
        if(data != null) {
            out.println();
        }
        out.println(".text");
        out.println();
        out.println(".global main");
        for (FunctionInstruction f : funcs){
            f.toAssembly(out);
        }
        program.toAssembly(out);
        for (LabelInstruction l : labels){
            l.toAssembly(out);
        }
    }
}
