/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;

/**
 *
 * @author hiran
 */
public class XMLPlistParserTest {
    private static final Logger log = LogManager.getLogger();
    
    public XMLPlistParserTest() {
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
     * Test of serialize method, of class XMLPlistParser.
     */
    @Test
    public void testSerialize() throws Exception {
        log.info("serialize");
    }

    /**
     * Test of parseListOfMaps method, of class XMLPlistParser.
     */
    @Test
    public void testParseListOfMaps() throws Exception {
        log.info("parseListOfMaps");
    }

    /**
     * Test of parseList method, of class XMLPlistParser.
     */
    @Test
    public void testParseList() throws Exception {
        log.info("parseList");
    }
    
}
