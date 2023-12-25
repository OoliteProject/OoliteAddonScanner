/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.plist.CountingErrorListener;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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
    
    private static void dumpPc(NSObject pc, String prefix) {
        System.out.print(prefix); System.out.println(pc.getClass().getName());
        if (pc instanceof NSDictionary) {
            for (Map.Entry<String, NSObject> pt: ((NSDictionary) pc).entrySet()) {
                dumpPT(pt.getValue(), prefix+"  ");
            }
        }
        if (pc instanceof NSArray) {
            for (NSObject pt: ((NSArray) pc).getArray()) {
                dumpPT(pt, prefix+"  ");
            }
        }
    }

    private static void dumpPT(NSObject pt, String prefix) {
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

        File f = new File(plistFilePath);

        try {
            NSObject pc = PropertyListParser.parse(f);
        
            log.info("Successfully parsed plist in {}", plistFilePath);
            if (log.isDebugEnabled()) {
                dumpPc(pc, "");
            }
        } catch (Exception e) {
            log.error("Could not parse " + f.getAbsolutePath());
        }
        
        
    }
}
