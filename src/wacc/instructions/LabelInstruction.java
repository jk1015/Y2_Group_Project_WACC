package wacc.instructions;


import java.io.PrintStream;

public class LabelInstruction implements Instruction{

    private String name;
    private String[] msg;

    public LabelInstruction(String name, String[] msg) {
        this.name = name;
        this.msg = msg;
    }

    @Override
    public void toAssembly(PrintStream out) {
        if (name.equals("putchar")){
            return;
        }
        out.println();
        out.println(name + ":");
        out.println("PUSH {lr}");
        assembly(out);
        out.println("POP {pc}");
    }

    public void assembly(PrintStream out){
        switch (name) {
            case "p_print_ln":
                out.println("LDR r0, =" + msg[0]);
                out.println("ADD r0, r0, #4");
                out.println("BL puts");
                out.println("MOV r0, #0");
                out.println("BL fflush");
                break;
            case "p_print_int":
                out.println("MOV r1, r0");
                out.println("LDR r0, =" + msg[0]);
                out.println("ADD r0, r0, #4");
                out.println("BL scanf");
                break;
            case "p_print_string":
                out.println("LDR r1, r[0]");
                out.println("ADD r2, r0, #4");
                out.println("LDR r0, =" + msg[0]);
                out.println("ADD r0, r0, #4");
                out.println("BL printf");
                out.println("MOV r0, #0");
                out.println("BL fflush");
                break;
            case "p_print_bool":
                out.println("CMP r0, #0");
                out.println("LDRNE r0, =" + msg[0]);
                out.println("LDREQ r0, =" + msg[1]);
                out.println("ADD r0, r0, #4");
                out.println("BL printf");
                out.println("MOV r0, #0");
                out.println("BL fflush");
                break;
        }

    }


}
