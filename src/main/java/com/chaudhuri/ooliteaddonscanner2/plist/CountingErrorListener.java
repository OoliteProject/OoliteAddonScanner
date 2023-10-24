/*
 */

package com.chaudhuri.ooliteaddonscanner2.plist;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An error listener that counts the number of occurrences.
 * 
 * @author hiran
 */
public class CountingErrorListener implements ANTLRErrorListener {
    private static final Logger log = LogManager.getLogger();

    private final List<String> syntaxErrors = new ArrayList<>();
    private final List<String> ambiguityErrors = new ArrayList<>();
    private final List<String> attemptFullContextErrors = new ArrayList<>();
    private final List<String> contextSensitivityErrors = new ArrayList<>();
    
    private String filename = "";
    
    public CountingErrorListener() {
    }
    
    public CountingErrorListener(String filename) {
        this.filename = filename;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException re) {
        log.debug("syntaxError({}, {}, {}, {}, {}, {})", recognizer, offendingSymbol, line, charPositionInLine, msg, re);

        String symbol = "";
        if (offendingSymbol instanceof CommonToken) {
            CommonToken token = (CommonToken)offendingSymbol;
            //ModelParser.VOCABULARY
            symbol = recognizer.getVocabulary().getSymbolicName(token.getType());
        }

        if (offendingSymbol != null) {
            log.error("Offending symbol: {} {} {}", offendingSymbol.getClass().getName(), symbol, offendingSymbol);
        }

        syntaxErrors.add("Syntax Error " + filename + "[" + line + ":" + charPositionInLine + "] " + msg);
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean bln, BitSet bitset, ATNConfigSet atncs) {
        log.debug("reportAmbiguity(...)");
        ambiguityErrors.add("a");
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitset, ATNConfigSet atncs) {
        log.debug("reportAttemptingFullContext(...)");
        attemptFullContextErrors.add("afc");
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atncs) {
        log.debug("reportContextSensitivity(...)");
        contextSensitivityErrors.add("cs");
    }
       
    /**
     * Returns whether errors were counted.
     * 
     * @return true if errors were counted, false otherwise
     */
    public boolean hasErrors() {
        return (syntaxErrors.size() + ambiguityErrors.size() + attemptFullContextErrors.size() + contextSensitivityErrors.size()) > 0;
    }

    /**
     * Returns the count of syntax errors.
     * 
     * @return the count
     */
    public int getSyntaxErrorCount() {
        return syntaxErrors.size();
    }

    /**
     * Returns the count of ambiguities.
     * 
     * @return the count
     */
    public int getAmbiguityCount() {
        return ambiguityErrors.size();
    }

    /**
     * Returns the count of attempted full contexts.
     * 
     * @return the count
     */
    public int getAttemptFullContextCount() {
        return attemptFullContextErrors.size();
    }

    /**
     * Returns the count of context sensitive errors.
     * 
     * @return the count
     */
    public int getContextSensitivityCount() {
        return contextSensitivityErrors.size();
    }

    /**
     * Returns the syntax errors.
     * 
     * @return the list of errors
     */
    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }

    /**
     * Returns the ambiguity errors.
     * 
     * @return the list of errors
     */
    public List<String> getAmbiguityErrors() {
        return ambiguityErrors;
    }

    /**
     * Returns the attempted full context errors.
     * 
     * @return the list of errors
     */
    public List<String> getAttemptFullContextErrors() {
        return attemptFullContextErrors;
    }

    /**
     * Returns the context sensitivity errors.
     * 
     * @return the list of errors
     */
    public List<String> getContextSensitivityErrors() {
        return contextSensitivityErrors;
    }

}
