package wacc.instructions;


import java.io.PrintStream;

public class LabelInstruction implements Instruction{

    private String name;

    public LabelInstruction(String name) {
        this.name = name;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.println(name);
        out.println("PUSH {lr}");
        // depend on the type

        out.println("ADD r0, r0, #4");
        out.println("BL printf");
        out.println("MOV r0, #0");
        out.println("POP {pc}");

    }

    public void assembly(PrintStream out){
        switch (name) {
            case "putchar" :
                break;
            case "p_print_ln":
                break;
            case "p_print_int":
                break;
            case "p_print_string":
                break;
            case "p_print_bool":
                break;
        }

    }


}
