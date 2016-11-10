package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

@SuppressWarnings("serial")
public class UndeclaredVariableException extends WACCCompilerException {

	public UndeclaredVariableException() {
		super();
	}
	
	public UndeclaredVariableException(String msg) {
		super(msg);
	}
	
	public UndeclaredVariableException(ParserRuleContext ctx) {
		super(ctx);
	}
	
	public UndeclaredVariableException(ParserRuleContext ctx, String msg) {
		super(ctx, msg);
	}

}
