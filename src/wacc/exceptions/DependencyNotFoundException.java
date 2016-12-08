package wacc.exceptions;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by jaspreet on 06/12/16.
 */
public class DependencyNotFoundException extends WACCSemanticErrorException {
    public DependencyNotFoundException() {
        super();
    }

    public DependencyNotFoundException(String msg) {
        super(msg);
    }

    public DependencyNotFoundException(ParserRuleContext ctx) {
        super(ctx);
    }

    public DependencyNotFoundException(ParserRuleContext ctx, String msg) {
        super(ctx, msg);
    }
}
