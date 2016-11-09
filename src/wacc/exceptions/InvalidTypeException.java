package wacc.exceptions;

import org.antlr.v4.runtime.misc.ParseCancellationException;

@SuppressWarnings("serial")
public class InvalidTypeException extends ParseCancellationException {

	public InvalidTypeException() {
		super();
	}
	
	public InvalidTypeException(String msg) {
		super(msg);
	}

}
