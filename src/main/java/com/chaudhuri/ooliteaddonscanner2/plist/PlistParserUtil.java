/*
 */
package com.chaudhuri.ooliteaddonscanner2.plist;

import com.chaudhuri.plist.PlistLexer;
import com.chaudhuri.plist.PlistParser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class PlistParserUtil {
    private static final Logger log = LogManager.getLogger();
    
    /**
     * Prevent instances from getting created.
     */
    private PlistParserUtil() {
    }
    
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

        return prepareParser(data, source, errorListener);
    }
    
    /**
     * Creates a parser prepared with throwing ErrorListener to read from
     * the given InputStream.
     * 
     * @param data the input stream to read from
     * @param source the source of the stream so we have good error messages
     * @return the created parser
     * @throws IOException something went wrong
     */
    public static PlistParser prepareParser(InputStream data, String source, ANTLRErrorListener errorListener) throws IOException {
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
    
    /**
     * Parses a plist dictionary or list from the given inputstream.
     * 
     * @param data the inputstream to read from
     * @param source the source of the data for good error messages
     * @return the parsed data
     * @throws IOException something went wrong
     */
    public static PlistParser.ParseContext parsePlist(InputStream data, String source) throws IOException {
        return prepareParser(data, source).parse();
    }
    
    /**
     * Parses a plist dictionary or list from the given inputstream.
     * 
     * @param data the inputstream to read from
     * @param source the source of the data for good error messages
     * @return the parsed data
     * @throws IOException something went wrong
     */
    public static PlistParser.ParseContext parsePlist(InputStream data, String source, ANTLRErrorListener errorListener) throws IOException {
        return prepareParser(data, source, errorListener).parse();
    }
    
    /**
     * Parses a list of strings from a valuecontext.
     * Used for e.g. parsing tags from a plist.
     * 
     * @param vc the valuecontext
     * @return the list of strings
     */
    public static List<String> getStringList(PlistParser.ValueContext vc) {
        log.debug("getStringList({})", vc);
        
        List<String> result = new ArrayList<>();
        for (int i=0; i < vc.getChildCount(); i++) {
            ParseTree child = vc.getChild(i);
            if (child instanceof PlistParser.ListContext) {
                PlistParser.ListContext lc = (PlistParser.ListContext)child;
                for (int j=0; j<lc.getChildCount(); j++) {
                    ParseTree listitem = lc.getChild(j);
                    if (listitem instanceof PlistParser.ValueContext) {
                        result.add(listitem.getText());
                    }
                }
            }
        }
        return result;
    }
}
