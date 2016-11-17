package wacc.instructions;

import java.io.PrintStream;

public interface Instruction {
	void toAssembly(PrintStream out);
}
