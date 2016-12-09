package wacc.instructions;

import wacc.LabelMaker;
import wacc.types.Type;

import java.io.PrintStream;

public class FuncRefIdentInstruction implements LocatableInstruction {

    private final String funcIdent;
    private final int register;
    private final Type type;

    public FuncRefIdentInstruction(String funcIdent, int register, Type type) {
        this.funcIdent = funcIdent;
        this.register = register;
        this.type = type;
    }

    @Override
    public void toAssembly(PrintStream out) {
        String funcLabel = LabelMaker.getFunctionLabel(funcIdent);
        out.println("LDR r" + register + ", =" + funcLabel);
    }

    @Override
    public String getLocationString() {
        return "[r" + register + "]";
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean usesRegister() {
        return true;
    }
}
