package wacc.instructions.expressions;

import wacc.instructions.Instruction;
import wacc.types.Type;

import java.io.PrintStream;

public abstract class ExprInstruction implements Instruction {

    private final int register;
    private final Type type;

    public ExprInstruction(int register, Type type) {
        this.register = register;
        this.type = type;
    }

    public Type getType() {
        return type;
    }


    public String getLocationString() {
        return "r" + register;
    }

    protected void commonAssembly(PrintStream out){
        String reg =getLocationString();
        out.println("MOV r0," + reg);
    }

}
