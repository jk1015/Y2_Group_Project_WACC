package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

import wacc.types.Type;

@SuppressWarnings("serial")
public class InvalidTypeException extends WACCCompilerException {

	public InvalidTypeException() {
		super();
	}

	public InvalidTypeException(ParserRuleContext ctx, String msg) {
		super(ctx, msg);
	}

	public InvalidTypeException(ParserRuleContext ctx) {
		super(ctx);
	}

	public InvalidTypeException(String msg) {
		super(msg);
	}

	public InvalidTypeException(Type exType, Type gotType) {
		super("Expected type " + exType + " got type " + gotType);
	}
	
	public InvalidTypeException(
			ParserRuleContext ctx, Type exType, Type gotType) {
		super(ctx, "Expected type " + exType + " got type " + gotType);
	}
}
