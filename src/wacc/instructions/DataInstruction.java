package wacc.instructions;

import java.io.PrintStream;

public class DataInstruction implements Instruction {

    private String ascii;
    private String name;

    public DataInstruction(  String name, String ascii) {
        this.ascii = ascii;
        this.name = name;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println(name + ":");
        out.println();
        out.println(".word " + ascii.length());
        out.println();
        out.println(".ascii " + ascii);
    }

}
