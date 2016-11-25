package wacc.instructions.locatable;

import wacc.instructions.Instruction;
import wacc.types.Type;

public interface LocatableInstruction extends Instruction {

    String getLocationString();
    Type getType();
}
