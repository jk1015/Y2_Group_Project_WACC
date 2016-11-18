package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

public class RedeclaredFunctionException extends WACCSemanticErrorException {
    public RedeclaredFunctionException() {
        super();
    }

    public RedeclaredFunctionException(String msg) {
        super(msg);
    }

    public RedeclaredFunctionException(ParserRuleContext ctx) {
        super(ctx);
    }

    public RedeclaredFunctionException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
}
