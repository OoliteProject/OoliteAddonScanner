/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.plist.PlistLexer;
import com.chaudhuri.plist.PlistParser;
import java.io.IOException;
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
public class PlistParserUtil {
    
    /**
     * Creates a parser prepared with throwing ErrorListener to read from
     * the given InputStream.
     * 
     * @param data the input stream to read from
     * @param source the source of the stream so we have good error messages
     * @return the created parser
     * @throws IOException something went wrong
     */
    public static PlistParser prepareParser(InputStream data, String source) throws IOException {
        ThrowingErrorListener errorListener = new ThrowingErrorListener();

        ReadableByteChannel channel = Channels.newChannel(data);
        CharStream charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8, 4096, CodingErrorAction.REPLACE, source, -1);
        PlistLexer lexer = new PlistLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        PlistParser parser = new PlistParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        
        return parser;
    }
    
    /**
     * Parses a plist list from the given inputstream.
     * 
     * @param data the inputstream to read from
     * @param source the source of the data for good error messages
     * @return the parsed list
     * @throws IOException something went wrong
     */
    public static PlistParser.ListContext parsePlistList(InputStream data, String source) throws IOException {
        return prepareParser(data, source).list();
    }

    /**
     * Parses a plist dictionary from the given inputstream.
     * 
     * @param data the inputstream to read from
     * @param source the source of the data for good error messages
     * @return the parsed dictionary
     * @throws IOException something went wrong
     */
    public static PlistParser.DictionaryContext parsePlistDictionary(InputStream data, String source) throws IOException {
        return prepareParser(data, source).dictionary();
    }
    
}
