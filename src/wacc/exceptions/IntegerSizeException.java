package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

@SuppressWarnings("serial")
public class IntegerSizeException extends WACCSyntaxErrorException {

	public IntegerSizeException() {
		super();
	}

	public IntegerSizeException(ParserRuleContext ctx, String msg) {
		super(ctx, msg);
	}

	public IntegerSizeException(ParserRuleContext ctx) {
		super(ctx);
	}

	public IntegerSizeException(String msg) {
		super(msg);
	}

    
}
