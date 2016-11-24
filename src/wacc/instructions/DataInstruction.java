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

    @Override
    public int hashCode() {
        return ascii.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataInstruction))
            return false;
        if (((DataInstruction) obj).getAscii().equals(ascii)){
            return true;
        }

        return false;
    }

    public String getName(){
        return name;
    }

    public String getAscii(){
        return ascii;
    }

}
