/*
 */
package com.chaudhuri.ooliteaddonscanner2.plist;

import com.chaudhuri.ooliteaddonscanner2.plist.PlistParserUtil;
import com.chaudhuri.plist.PlistParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hiran
 */
public class PlistParserUtilTest {
    public static final Logger log = LogManager.getLogger();
    
    public PlistParserUtilTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of prepareParser method, of class PlistParserUtil.
     */
    @Test
    public void testPrepareParser() throws Exception {
        log.info("prepareParser");
        
        URL url = getClass().getResource("/PlistParserUtilTest/dictionary1.plist");
        
        PlistParser pp = PlistParserUtil.prepareParser(url.openStream(), url.toString());
        assertNotNull(pp);
    }

    /**
     * Test of parsePlistList method, of class PlistParserUtil.
     */
    @Test
    public void testParsePlistList() throws Exception {
        log.info("parsePlistList");

        URL url = getClass().getResource("/PlistParserUtilTest/list1.plist");
        
        PlistParser.ListContext list = PlistParserUtil.parsePlistList(url.openStream(), url.toString());
        assertNotNull(list);
    }

    /**
     * Test of parsePlistDictionary method, of class PlistParserUtil.
     */
    @Test
    public void testParsePlistDictionary() throws Exception {
        log.info("parsePlistDictionary");

        {
            URL url = getClass().getResource("/PlistParserUtilTest/dictionary1.plist");

            PlistParser.DictionaryContext dict = PlistParserUtil.parsePlistDictionary(url.openStream(), url.toString());
            assertNotNull(dict);
        }
    }
    
    /**
     * Test of comments.
     */
    @Test
    public void testParseComments1() throws Exception {
        log.info("parseComments1");

        File f = new File("src/test/data/comments/keyconfig.plist");
        PlistParser.DictionaryContext dict = PlistParserUtil.parsePlistDictionary(new FileInputStream(f), f.getPath());
        assertNotNull(dict, "Comment problem in keyconfig.plist");
    }
    
    /**
     * Test of comments.
     */
    @Test
    public void testParseComments2() throws Exception {
        log.info("parseComments2");

        File f = new File("src/test/data/comments/missiontext.plist");
        PlistParser.DictionaryContext dict = PlistParserUtil.parsePlistDictionary(new FileInputStream(f), f.getPath());
        assertNotNull(dict, "Comment problem in missiontext.plist");
    }
}
