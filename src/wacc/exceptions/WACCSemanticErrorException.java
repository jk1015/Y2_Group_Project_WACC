package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class WACCSemanticErrorException extends WACCCompilerException {
    public WACCSemanticErrorException() {
        super();
    }

    public WACCSemanticErrorException(String msg) {
        super(msg);
    }

    public WACCSemanticErrorException(ParserRuleContext ctx) {
        super(ctx);
    }

    public WACCSemanticErrorException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
}
