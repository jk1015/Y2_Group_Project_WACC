package wacc.instructions.expressions.baseExpressions;

import wacc.instructions.DataInstruction;
import wacc.instructions.expressions.ExprInstruction;
import wacc.types.PrimType;

import java.io.PrintStream;

/**
 * Created by jk1015 on 22/11/16.
 */
public class StringLiterInstruction extends ExprInstruction {

    private String stringLiter;
    private int location;
    //TODO
    //print string at top of file
    public StringLiterInstruction(int count, int register, String stringLiter) {
        super(register, PrimType.STRING);
        this.location = count;
        this.stringLiter = stringLiter;
    }

    @Override
    public void toAssembly(PrintStream out) {
        out.print("LDR " + getLocationString() + ", ");
        out.println("=msg_" + location);
    }

    public DataInstruction setData(String ascii) {
        String nameOfMsg = "msg_" + location;
        DataInstruction dataInstruction = new DataInstruction(nameOfMsg, ascii);
        location++;
        return dataInstruction;
    }

    public String getStringLiter() {
        return stringLiter;
    }
}
