package wacc.instructions;

import java.io.PrintStream;

public class InitAssignInstruction implements Instruction{

    private LocatableInstruction location;
    private String var;

    public InitAssignInstruction(LocatableInstruction location, String var) {
        this.location = location;
        this.var = var;
    }


    @Override
    public void toAssembly(PrintStream out) {
        location.toAssembly(out);
        out.println("SUB sp, sp, #4");
        out.println("STR " + location.getLocationString() + ", " + var);
    }
}
