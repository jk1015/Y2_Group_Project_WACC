package wacc.instructions;

import wacc.types.Type;

public interface LocatableInstruction extends Instruction {

    String getLocationString();
    Type getType();
    boolean usesRegister();
}
