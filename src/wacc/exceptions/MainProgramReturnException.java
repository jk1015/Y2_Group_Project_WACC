package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class MainProgramReturnException extends WACCSemanticErrorException {

	public MainProgramReturnException() {
		super();
	}

	public MainProgramReturnException(ParserRuleContext ctx, String msg) {
		super(ctx, msg);
	}

	public MainProgramReturnException(ParserRuleContext ctx) {
		super(ctx);
	}

	public MainProgramReturnException(String msg) {
		super(msg);
	}

}
