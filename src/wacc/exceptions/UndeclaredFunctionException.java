package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;


public class UndeclaredFunctionException extends WACCSemanticErrorException {
    public UndeclaredFunctionException() {
        super();
    }

    public UndeclaredFunctionException(String msg) {
        super(msg);
    }

    public UndeclaredFunctionException(ParserRuleContext ctx) {
        super(ctx);
    }

    public UndeclaredFunctionException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
}
