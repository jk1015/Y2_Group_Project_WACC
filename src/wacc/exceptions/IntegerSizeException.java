package wacc.exceptions;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class IntegerSizeException extends ParseCancellationException {

    public IntegerSizeException() {
        super();
    }

    public IntegerSizeException(String msg) {
        super(msg);
    }
}
