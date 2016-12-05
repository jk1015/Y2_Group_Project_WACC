package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import wacc.WACCErrorListener;

@SuppressWarnings("serial")
public abstract class WACCCompilerException extends ParseCancellationException {

	public WACCCompilerException() {
		super();
	}
	
	public WACCCompilerException(String msg) {
		super(msg);
	}
	
	public WACCCompilerException(ParserRuleContext ctx) {
		super(ctx.getStart().getLine() + ":" +
				ctx.getStart().getCharPositionInLine() + ":");
	}
	
	public WACCCompilerException(ParserRuleContext ctx, String msg) {
		super(ctx.getStart().getLine() + ":" +
				ctx.getStart().getCharPositionInLine() + ": " + msg);
	}
	
}
