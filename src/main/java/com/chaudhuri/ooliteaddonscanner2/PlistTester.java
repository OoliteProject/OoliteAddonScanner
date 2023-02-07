/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.PlistLexer;
import com.chaudhuri.PlistParser;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses the plist file given on the command line.
 * 
 * @author hiran
 */
public class PlistTester {
    private static final Logger log = LoggerFactory.getLogger(PlistTester.class);

    //private static String plistFilePath = "src/test/data/cholmondely.plist";
    private static String plistFilePath = "/home/hiran/test/oolite.oxp.Cholmondeley.Carver's_Anarchy-Ships_Library.oxp/oolite.oxp.Cholmondeley.Carver's_Anarchy-Ships_Library.oxp/Config/missiontext.plist";
    
    private static void dumpPc(PlistParser.ParseContext pc, String prefix) {
        System.out.print(prefix); System.out.println(pc.getClass().getName());
        for (ParseTree pt: pc.children) {
            dumpPT(pt, prefix+"  ");
        }
    }

    private static void dumpPT(ParseTree pt, String prefix) {
        System.out.print(prefix); System.out.println(pt.getClass().getName());
    }
    
    private static class ErrorListener implements ANTLRErrorListener {
        
        private int syntaxErrorCount;
        private int ambiguityCount;
        private int attemptFullContextCount;
        private int contextSensitivityCount;

        @Override
        public void syntaxError(Recognizer<?, ?> rcgnzr, Object o, int i, int i1, String string, RecognitionException re) {
            syntaxErrorCount++;
        }

        @Override
        public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean bln, BitSet bitset, ATNConfigSet atncs) {
            ambiguityCount++;
        }

        @Override
        public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitset, ATNConfigSet atncs) {
            attemptFullContextCount++;
        }

        @Override
        public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atncs) {
            contextSensitivityCount++;
        }
        
        public boolean hasErrors() {
            return (syntaxErrorCount + ambiguityCount + attemptFullContextCount + contextSensitivityCount) > 0;
        }

        public int getSyntaxErrorCount() {
            return syntaxErrorCount;
        }

        public int getAmbiguityCount() {
            return ambiguityCount;
        }

        public int getAttemptFullContextCount() {
            return attemptFullContextCount;
        }

        public int getContextSensitivityCount() {
            return contextSensitivityCount;
        }
        
        
    }

    public static void main(String[] args) throws Exception {
        log.debug("main(...)");
        if (args.length >0) {
            plistFilePath = args[0];
        }

        ErrorListener el = new ErrorListener();
        
        InputStream in = new FileInputStream(plistFilePath);
        ReadableByteChannel channel = Channels.newChannel(in);
        CharStream charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8, 4096, CodingErrorAction.REPLACE, plistFilePath, -1);
        PlistLexer lexer = new PlistLexer(charStream);
        lexer.addErrorListener(el);
        //lexer.getAllTokens();
        
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        PlistParser parser = new PlistParser(tokenStream);
        parser.addErrorListener(el);
        PlistParser.ParseContext pc = parser.parse();
        
        if (el.hasErrors()) {
            log.error("Found errors in {}", plistFilePath);
            log.error(String.format("Syntax Errors:          %4d", el.getSyntaxErrorCount()));
            log.error(String.format("Ambiguities:            %4d", el.getAmbiguityCount()));
            log.error(String.format("Context Errors:         %4d", el.getContextSensitivityCount()));
            log.error(String.format("Attempted Full Context: %4d", el.getAttemptFullContextCount()));
        } else {
            log.info("Successfully parsed plist in {}", plistFilePath);
            if (log.isDebugEnabled()) {
                dumpPc(pc, "");
            }
        }
        
        
    }
}
