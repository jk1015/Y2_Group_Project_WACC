package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by ad5115 on 05/12/16.
 */
public class InvalidBreakOrContinueException extends WACCSemanticErrorException{
    public InvalidBreakOrContinueException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
}
