package wacc.instructions;

import java.io.PrintStream;

/**
 * Created by jk1015 on 23/11/16.
 */
public class IdentifierInstruction implements Instruction {

    private final String location;

    public IdentifierInstruction(String location) {
        this.location = location;
    }


    @Override
    public void toAssembly(PrintStream out) {
        out.println(location);
    }
}
