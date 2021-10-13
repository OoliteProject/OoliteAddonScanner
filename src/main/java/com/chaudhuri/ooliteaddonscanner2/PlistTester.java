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
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hiran
 */
public class PlistTester {
    private static final Logger log = LoggerFactory.getLogger(PlistTester.class);

    //private static final String plistFilePath = "/home/hiran/GNUstep/Library/Caches/org.aegidian.oolite/Oolite-manifests.plist";
    private static final String plistFilePath = "src/test/data/cholmondely.plist";
    
    private static void dumpPc(PlistParser.ParseContext pc, String prefix) {
        System.out.print(prefix); System.out.println(pc.getClass().getName());
        for (ParseTree pt: pc.children) {
            dumpPT(pt, prefix+"  ");
        }
    }

    private static void dumpPT(ParseTree pt, String prefix) {
        System.out.print(prefix); System.out.println(pt.getClass().getName());
    }

    public static void main(String[] args) throws Exception {
        log.debug("main(...)");
        
        InputStream in = new FileInputStream(plistFilePath);
        ReadableByteChannel channel = Channels.newChannel(in);
        CharStream charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8, 4096, CodingErrorAction.REPLACE, plistFilePath, -1);
        PlistLexer lexer = new PlistLexer(charStream);
        //lexer.getAllTokens();
        
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        PlistParser parser = new PlistParser(tokenStream);
        PlistParser.ParseContext pc = parser.parse();
        
        log.debug("Parsed plist in {}", plistFilePath);
        
        dumpPc(pc, "");
    }
}
