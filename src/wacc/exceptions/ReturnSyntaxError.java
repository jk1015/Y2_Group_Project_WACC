package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by jaspreet on 10/11/16.
 */
public class ReturnSyntaxError extends WACCCompilerException {

    public ReturnSyntaxError() {
        super();
    }

    public ReturnSyntaxError(String msg) {
        super(msg);
    }

    public ReturnSyntaxError(ParserRuleContext ctx) {
        super(ctx);
    }

    public ReturnSyntaxError(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
    
}
