/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
        
        DocumentBuilder db = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("root");
        root.setAttribute("id", "myid");
        doc.appendChild(root);
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root id=\"myid\"/>", XMLPlistParser.serialize(doc));
    }

    /**
     * Test of parseListOfMaps method, of class XMLPlistParser.
     */
    @Test
    public void testParseListOfMaps() throws Exception {
        log.info("parseListOfMaps");
    }
    
    private static class TestErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException saxpe) throws SAXException {
            log.error("warning({})", saxpe);
        }

        @Override
        public void error(SAXParseException saxpe) throws SAXException {
            log.error("error({})", saxpe);
        }

        @Override
        public void fatalError(SAXParseException saxpe) throws SAXException {
            log.error("fatalError({})", saxpe);
        }
        
    }

    /**
     * Test of parseList method, of class XMLPlistParser.
     */
    @Test
    public void testParseList() throws Exception {
        log.info("parseList");

        {
            URL url = getClass().getResource("/XMLPlistParserTest/XmlPlist1.xml");
            InputStream in = url.openStream();

            try {
                List<Object> list = XMLPlistParser.parseList(in, null);
                fail("expected exception");
            } catch(IllegalArgumentException e) {
                assertEquals("Expected root node plist", e.getMessage());
                log.debug("caught expected exception", e);
            }
        }
        {
            URL url = getClass().getResource("/XMLPlistParserTest/XmlPlist2.xml");
            InputStream in = url.openStream();

            try {
                List<Object> list = XMLPlistParser.parseList(in, null);
                fail("expected exception");
            } catch(IllegalArgumentException e) {
                assertEquals("Expected plist 1.0 format", e.getMessage());
                log.debug("caught expected exception", e);
            }
        }
        {
            URL url = getClass().getResource("/XMLPlistParserTest/XmlPlist3.xml");
            InputStream in = url.openStream();

            TestErrorHandler teh = new TestErrorHandler();

            List<Object> list = XMLPlistParser.parseList(in, teh);
            assertNotNull(list);
            assertEquals(0, list.size());
        }
        {
            URL url = getClass().getResource("/XMLPlistParserTest/XmlPlist4.xml");
            InputStream in = url.openStream();

            TestErrorHandler teh = new TestErrorHandler();
            try {
                List<Object> list = XMLPlistParser.parseList(in, teh);
                fail("expected exception");
            } catch(IllegalArgumentException e) {
                assertEquals("Could not parse element blah", e.getMessage());
                assertEquals("Unknown element blah", e.getCause().getMessage());
                log.debug("caught expected exception", e);
            }
        }
        {
            URL url = getClass().getResource("/XMLPlistParserTest/XmlPlist5.xml");
            InputStream in = url.openStream();

            TestErrorHandler teh = new TestErrorHandler();
            try {
                List<Object> list = XMLPlistParser.parseList(in, teh);
                fail("expected exception");
            } catch(IllegalArgumentException e) {
                assertEquals("Could not parse element dict", e.getMessage());
                assertEquals("Expected element 'key'", e.getCause().getMessage());
                log.debug("caught expected exception", e);
            }
        }
        {
            URL url = getClass().getResource("/XMLPlistParserTest/XmlPlist6.xml");
            InputStream in = url.openStream();

            TestErrorHandler teh = new TestErrorHandler();

            List<Object> list = XMLPlistParser.parseList(in, teh);
            assertNotNull(list);
            assertEquals(8, list.size());
        }
    }
    
}
