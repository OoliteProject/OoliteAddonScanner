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

/**
 *
 * @author hiran
 */
public class PlistTester {

    private static final String plistFilePath = "/home/hiran/GNUstep/Library/Caches/org.aegidian.oolite/Oolite-manifests.plist";

    public static void main(String[] args) throws Exception {
        
        InputStream in = new FileInputStream(plistFilePath);
        ReadableByteChannel channel = Channels.newChannel(in);
        CharStream charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8, 4096, CodingErrorAction.REPLACE, plistFilePath, -1);
        PlistLexer lexer = new PlistLexer(charStream);
        //lexer.getAllTokens();
        
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        PlistParser parser = new PlistParser(tokenStream);
        parser.parse();
    }
}
