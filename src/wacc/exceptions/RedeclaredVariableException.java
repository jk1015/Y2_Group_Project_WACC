package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

@SuppressWarnings("serial")
public class RedeclaredVariableException extends WACCSemanticErrorException {

	public RedeclaredVariableException() {
		super();
	}

	public RedeclaredVariableException(ParserRuleContext ctx, String msg) {
		super(ctx, msg);
	}

	public RedeclaredVariableException(ParserRuleContext ctx) {
		super(ctx);
	}

	public RedeclaredVariableException(String msg) {
		super(msg);
	}

}
