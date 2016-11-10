package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class InvalidReturnException extends WACCSyntaxErrorException {

    public InvalidReturnException() {
        super();
    }

    public InvalidReturnException(String msg) {
        super(msg);
    }

    public InvalidReturnException(ParserRuleContext ctx) {
        super(ctx);
    }

    public InvalidReturnException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
    
}
