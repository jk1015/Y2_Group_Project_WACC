package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class WACCSyntaxErrorException extends WACCCompilerException {
    public WACCSyntaxErrorException() {
        super();
    }

    public WACCSyntaxErrorException(String msg) {
        super(msg);
    }

    public WACCSyntaxErrorException(ParserRuleContext ctx) {
        super(ctx);
    }

    public WACCSyntaxErrorException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
}
