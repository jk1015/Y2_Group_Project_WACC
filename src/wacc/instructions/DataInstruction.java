package wacc.instructions;

import java.io.PrintStream;

public class DataInstruction implements Instruction {

    private String ascii;
    private String name;

    public DataInstruction(String name, String ascii) {
        this.ascii = ascii;
        this.name = name;
    }

    @Override
    public void toAssembly(PrintStream out) {
        String checkLength =ascii.replaceAll("\\\\","");

        out.println();
        out.println(name + ":");
        out.println(".word " + (checkLength.length()-2));
        out.println(".ascii " + ascii);
    }

}
