/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenFactory;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hiran
 */
public class ThrowingErrorListenerTest {
    private static final Logger log = LogManager.getLogger();
    
    public ThrowingErrorListenerTest() {
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
     * Test of syntaxError method, of class ThrowingErrorListener.
     */
    @Test
    public void testSyntaxError() {
        log.info("syntaxError");
        
        ThrowingErrorListener tel = new ThrowingErrorListener();
        try {
            tel.syntaxError(null, null, 0, 0, null, null);
            fail("expected exception");
        } catch (ParseCancellationException e) {
            assertEquals("line 0:0 null null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
    /**
     * Test of syntaxError method, of class ThrowingErrorListener.
     */
    @Test
    public void testSyntaxError2() {
        log.info("syntaxError2");
        
        ThrowingErrorListener tel = new ThrowingErrorListener();
        String offendingSymbol = "symbol";
        try {
            tel.syntaxError(null, offendingSymbol, 0, 0, null, null);
            fail("expected exception");
        } catch (ParseCancellationException e) {
            assertEquals("line 0:0 symbol null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
    /**
     * Test of syntaxError method, of class ThrowingErrorListener.
     */
    @Test
    public void testSyntaxError3() {
        log.info("syntaxError3");
        
        ThrowingErrorListener tel = new ThrowingErrorListener();
        CommonToken offendingSymbol = new CommonToken(0);
        try {
            tel.syntaxError(null, offendingSymbol, 0, 0, null, null);
            fail("expected exception");
        } catch (NullPointerException e) {
            /*
            Different JVMs seem to produce different messages, so we cannot
            reliably tell which message to check for.
            
            //assertNull(e.getMessage());
            //assertEquals("Cannot invoke \"org.antlr.v4.runtime.Recognizer.getVocabulary()\" because \"recognizer\" is null", e.getMessage());
             */
            log.debug("caught expected exception", e);
        }
    }
    
    private class TestRecognizer extends Recognizer {

        /**
         * This method is marked as deprecated but we have to implement it?
         * @return 
         */
        @Override
        public String[] getTokenNames() {
            return new String[]{"zero", "one", "two"};
        }

        @Override
        public String[] getRuleNames() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public String getGrammarFileName() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public ATN getATN() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public IntStream getInputStream() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void setInputStream(IntStream stream) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public TokenFactory getTokenFactory() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void setTokenFactory(TokenFactory tf) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        
    }
    
    /**
     * Test of syntaxError method, of class ThrowingErrorListener.
     */
    @Test
    public void testSyntaxError4() {
        log.info("syntaxError4");
        
        ThrowingErrorListener tel = new ThrowingErrorListener();
        CommonToken offendingSymbol = new CommonToken(0);
        TestRecognizer recognizer = new TestRecognizer();
        try {
            tel.syntaxError(recognizer, offendingSymbol, 0, 0, null, null);
            fail("expected exception");
        } catch (ParseCancellationException e) {
            assertEquals("line 0:0 [@-1,0:0='<no text>',<0>,0:-1] null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
}
