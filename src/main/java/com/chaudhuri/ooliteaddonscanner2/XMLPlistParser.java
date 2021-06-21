/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** Parses plist stored as XML.
 *
 * @author hiran
 */
public class XMLPlistParser {
    private static final Logger log = LoggerFactory.getLogger(XMLPlistParser.class);
    
    public static class MySaxErrorHandler implements ErrorHandler {
        private Expansion expansion;
        
        public MySaxErrorHandler(Expansion expansion) {
            this.expansion = expansion;
        }

        @Override
        public void warning(SAXParseException saxpe) throws SAXException {
            expansion.addWarning(String.format("%s (Warning)", saxpe.getMessage()));
        }

        @Override
        public void error(SAXParseException saxpe) throws SAXException {
            expansion.addWarning(String.format("%s (Error)", saxpe.getMessage()));
        }

        @Override
        public void fatalError(SAXParseException saxpe) throws SAXException {
            // these conditions will make the parser throw an exception, which is added to the index anyway
            //expansion.addWarning(String.format("%s (Fatal)", saxpe.getMessage()));
        }
        
    }
    
    public static String serialize(Node n) throws TransformerConfigurationException, TransformerException {
        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newDefaultInstance().newTransformer();
        t.transform(new DOMSource(n), new StreamResult(sw));
        return sw.toString();
    }
    
    private static List parseArray(Element array) {
        log.debug("parseArray({})", array);
        
        ArrayList result = new ArrayList();
        NodeList nl = array.getChildNodes();
        for (int i=0;i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                result.add(parseElement((Element)n));
            }
        }
        
        return result;
    }
    
    private static Map<String, Object> parseDict(Element dict) {
        log.debug("parseDict({})", dict);
        
        Map<String, Object> result = new TreeMap<>();
        NodeList nl = dict.getChildNodes();
        
        String key = null;
        
        for (int i=0;i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                if (key == null) {
                    // parse the key
                    key = String.valueOf(parseElement((Element)n));
                } else {
                    // do something about the value
                    Object value = parseElement((Element)n);
                    result.put(key, value);
                    key = null;
                }
            }
        }
        
        return result;
    }
    
    private static Object parseElement(Element e) {
        log.debug("parseElement({})", e);
        switch (e.getTagName()) {
            case "array":
                return parseArray(e);
            case "integer":
                return Integer.parseInt(e.getTextContent());
            case "real":
                return Double.parseDouble(e.getTextContent());
            case "string":
            case "key":
                return e.getTextContent();
            case "dict":
                return parseDict(e);
            case "true":
                return Boolean.TRUE;
            case "false":
                return Boolean.FALSE;
            default:
                throw new IllegalArgumentException(String.format("Unknown element %s", e.getTagName()));
        }
    }
    
    public static Map<String, Object> parseListOfMaps(InputStream in, ErrorHandler eh) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        log.debug("parseMap({})", in);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        if(eh != null) {
            db.setErrorHandler(eh);
        }
        Document doc = db.parse(in);
        Element plist = doc.getDocumentElement();
        if (!"plist".equals(plist.getNodeName())) {
            throw new IllegalArgumentException("Expected root node plist");
        }
        if (!"1.0".equals(plist.getAttribute("version"))) {
            throw new IllegalArgumentException("Expected plist 1.0 format");
        }

        //log.debug("Parsed {}", serialize(doc));
        List<Map<String, Object>> result = new ArrayList<>();
        NodeList nl = plist.getChildNodes();
        for (int i= 0; i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                result.add(parseDict((Element)n));
            }
        }
        
        if (result.size() != 1) {
            throw new IllegalArgumentException("Not exactly one map in document?");
        }
        
        return (Map<String, Object>)result.get(0);
    }

    /** Parses an XML file, checks it is a plist and returns the list of values
     * found.
     * 
     * @param in
     * @param eh
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException 
     */
    public static List parseList(InputStream in, ErrorHandler eh) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        log.debug("parseList({})", in);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        if(eh != null) {
            db.setErrorHandler(eh);
        }
        Document doc = db.parse(in);
        Element plist = doc.getDocumentElement();
        if (!"plist".equals(plist.getNodeName())) {
            throw new IllegalArgumentException("Expected root node plist");
        }
        if (!"1.0".equals(plist.getAttribute("version"))) {
            throw new IllegalArgumentException("Expected plist 1.0 format");
        }

        log.debug("Parsed {}", serialize(doc));
        ArrayList result = new ArrayList();
        NodeList nl = plist.getChildNodes();
        for (int i= 0; i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                result.add(parseElement((Element)n));
            }
        }
        
        return result;
    }
}
