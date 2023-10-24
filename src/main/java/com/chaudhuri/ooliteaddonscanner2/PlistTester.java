/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.plist.CountingErrorListener;
import com.chaudhuri.plist.PlistLexer;
import com.chaudhuri.plist.PlistParser;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parses the plist file given on the command line.
 * 
 * @author hiran
 */
public class PlistTester {
    private static final Logger log = LogManager.getLogger(PlistTester.class);

    private static String plistFilePath = "src/test/data/cholmondely.plist";
    
    private static void dumpPc(PlistParser.ParseContext pc, String prefix) {
        System.out.print(prefix); System.out.println(pc.getClass().getName());
        for (ParseTree pt: pc.children) {
            dumpPT(pt, prefix+"  ");
        }
    }

    private static void dumpPT(ParseTree pt, String prefix) {
        System.out.print(prefix); System.out.println(pt.getClass().getName());
    }

    /**
     * Main entry point for the batch version of the plist tester.
     * 
     * @param args command line arguments
     * @throws Exception something went wrong
     */
    public static void main(String[] args) throws Exception {
        log.debug("main(...)");
        if (args.length >0) {
            plistFilePath = args[0];
        }

        CountingErrorListener el = new CountingErrorListener();
        
        InputStream in = new FileInputStream(plistFilePath);
        ReadableByteChannel channel = Channels.newChannel(in);
        CharStream charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8, 4096, CodingErrorAction.REPLACE, plistFilePath, -1);
        PlistLexer lexer = new PlistLexer(charStream);
        lexer.addErrorListener(el);
        
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        PlistParser parser = new PlistParser(tokenStream);
        parser.addErrorListener(el);
        PlistParser.ParseContext pc = parser.parse();
        
        if (el.hasErrors()) {
            if (log.isErrorEnabled()) {
                log.error("Found errors in {}", plistFilePath);
                log.error("Syntax Errors:          {}", el.getSyntaxErrorCount());
                log.error(String.format("Ambiguities:            %4d", el.getAmbiguityCount()));
                log.error(String.format("Context Errors:         %4d", el.getContextSensitivityCount()));
                log.error(String.format("Attempted Full Context: %4d", el.getAttemptFullContextCount()));
            }
        } else {
            log.info("Successfully parsed plist in {}", plistFilePath);
            if (log.isDebugEnabled()) {
                dumpPc(pc, "");
            }
        }
        
        
    }
}
