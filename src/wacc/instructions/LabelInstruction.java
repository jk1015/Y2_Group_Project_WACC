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
        if (name.equals("p_throw_overflow_error") || name.equals("p_throw_runtime_error")){
            out.println();
            out.println(name + ":");
            assembly(out);
            return;
        }
        out.println();
        out.println(name + ":");
        out.println("PUSH {lr}");
        assembly(out);
        out.println("POP {pc}");
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LabelInstruction))
            return false;
        if (((LabelInstruction) obj).getName().equals(name)){
            return true;
        }

        return false;
    }

    public String getName(){
        return name;
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
            case "p_read_char":
            case "p_read_int":
                out.println("MOV r1, r0");
                out.println("LDR r0, =" + msg[0]);
                out.println("ADD r0, r0, #4");
                out.println("BL scanf");
                break;
            case "p_print_int":
                out.println("MOV r1, r0");
                out.println("LDR r0, =" + msg[0]);
                out.println("ADD r0, r0, #4");
                out.println("BL printf");
                out.println("MOV r0, #0");
                out.println("BL fflush");
                break;
            case "p_print_string":
                out.println("LDR r1, [r0]");
                out.println("ADD r2, r0, #4");
                if (msg.length<2){
                    out.println("LDR r0, =" + msg[0]);
                }else {
                    out.println("LDR r0, =" + msg[1]);
                }
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
            case "p_throw_overflow_error":
                out.println("LDR r0, =" + msg[0]);
                out.println("BL p_throw_runtime_error");
                break;
            case "p_check_divide_by_zero":
                out.println("CMP r1, #0");
                out.println("LDREQ r0, =" + msg[0]);
                out.println("BLEQ p_throw_runtime_error");
                break;
            case "p_throw_runtime_error":
                out.println("BL p_print_string");
                out.println("MOV r0, #-1");
                out.println("BL exit");
                break;
        }

    }


}
