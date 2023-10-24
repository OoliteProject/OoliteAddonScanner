/*
 */
package com.chaudhuri.ooliteaddonscanner2.plist;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class ThrowingErrorListener extends BaseErrorListener {
    private static Logger log = LogManager.getLogger(ThrowingErrorListener.class);
    
    public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();
    
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        log.error("syntaxError({}, {}, {}, {}, {}, {})", recognizer, offendingSymbol, line, charPositionInLine, msg, e);

        String symbol = "";
        if (offendingSymbol instanceof CommonToken) {
            CommonToken token = (CommonToken)offendingSymbol;
            //ModelParser.VOCABULARY
            symbol = recognizer.getVocabulary().getSymbolicName(token.getType());
        }

        if (offendingSymbol != null) {
            log.error("Offending symbol: {} {} {}", offendingSymbol.getClass().getName(), symbol, offendingSymbol);
        }

        throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + offendingSymbol + " " + msg, e);
    }
    
}
