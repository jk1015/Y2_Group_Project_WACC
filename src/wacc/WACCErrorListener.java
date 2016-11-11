package wacc;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import wacc.exceptions.WACCSyntaxErrorException;

public class WACCErrorListener extends BaseErrorListener {

    public static final WACCErrorListener INSTANCE = new WACCErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, 
                            String msg, RecognitionException e)
            throws ParseCancellationException {
        throw new WACCSyntaxErrorException("line " + line + ":" + charPositionInLine + " " + msg);
    }
}
