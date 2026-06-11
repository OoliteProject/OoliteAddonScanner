/*
 */
package com.chaudhuri.ooliteaddonscanner2.plist;

import com.chaudhuri.plist.PlistBaseListener;
import com.chaudhuri.plist.PlistParser;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Validates a plist Dictionary and collects the warnings.
 * Apply this listener via ParseTreeWalker, then retrieve the collected
 * warnings.
 * 
 * @author oocube
 */
public class ValidationListener extends PlistBaseListener {
    private static final Logger log = LogManager.getLogger();
    
    private final List<String> warnings;
    
    /**
     * Creates a new instance.
     */
    public ValidationListener() {
        warnings = new ArrayList<>();
    }

    /**
     * check the last element is a semicolon.
     * @param ctx 
     */
    @Override
    public void exitDictionary(PlistParser.DictionaryContext ctx) {
        log.debug("exitDictionary({})", ctx);
        if (ctx.children == null) {
            log.warn("plist dictionary without content?");
            return;
        }
        if (ctx.children.size()<2) {
            log.warn("plist dictionary without braces?");
            return;
        }
        int posSEMI = ctx.children.size() -2;
        ParseTree pt = ctx.children.get(posSEMI);
        log.debug("semicolon: {}", pt);
        if (pt.getPayload() instanceof Token token) {
            if (token.getType() == PlistParser.LBRACE) {
                // we are looking at an empty plist. we are good
                return;
            }
            if (token.getType() == PlistParser.SEMI) {
                // assumed semicolon found. we are good
                return;
            }
        }
        
        TokenSource ts = ctx.getStop().getTokenSource();
        String msg = String.format("Expected semicolon in %s after dictionary entry at [%s:%s]", ts.getSourceName(), ts.getLine(), ts.getCharPositionInLine());
        warnings.add(msg);
    }

    /**
     * Returns the collected warnings.
     * 
     * @return the warnings
     */
    public List<String> getWarnings() {
        return new ArrayList<String>(warnings);
    }
    
}
