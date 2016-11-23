package wacc.instructions;

import java.io.PrintStream;

public class FunctionInstruction implements Instruction {

    private String functionLabel;
    private Instruction statement;

    public FunctionInstruction(String functionLabel, Instruction statement) {
        this.functionLabel = functionLabel;
        this.statement = statement;
    }

    @Override
    public void toAssembly(PrintStream out) {
        // label
        out.println(functionLabel);

        // store link addr
        out.println("PUSH {lr}");

        // function contents
        statement.toAssembly(out);

        // set pc
        out.println("POP {pc}");
        out.println(".ltorg");
    }
}
