package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class IncompleteReturnException extends WACCSyntaxErrorException {
    public IncompleteReturnException() {
        super();
    }

    public IncompleteReturnException(String msg) {
        super(msg);
    }

    public IncompleteReturnException(ParserRuleContext ctx) {
        super(ctx);
    }

    public IncompleteReturnException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
}
