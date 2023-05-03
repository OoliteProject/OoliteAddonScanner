/*
 */
package com.chaudhuri.ooliteaddonscanner2;

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
public class RegistryExceptionTest {
    private static final Logger log = LogManager.getLogger();
    
    public RegistryExceptionTest() {
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

    @Test
    public void testRegistryException_String() {
        log.info("testRegistryException_String");
        
        Exception e = new RegistryException("mymessage");
        assertEquals("mymessage", e.getMessage());
    }

    @Test
    public void testRegistryException_Throwable() {
        log.info("testRegistryException_Throwable");
        
        Exception cause = new Exception("cause");
        Exception e = new RegistryException(cause);
        assertEquals(cause, e.getCause());
    }

    @Test
    public void testRegistryException_String_Throwable() {
        log.info("testRegistryException_String_Throwable");
        
        Exception cause = new Exception("cause");
        Exception e = new RegistryException("cause", cause);
        assertEquals("cause", e.getMessage());
        assertEquals(cause, e.getCause());
    }
    
    @Test
    public void testRegistryException_String_Throwable_boolean_boolean() {
        log.info("testRegistryException_String_Throwable_boolean_boolean");
        
        Exception cause = new Exception("cause");
        Exception e = new RegistryException("cause", cause, true, true);
        assertEquals("cause", e.getMessage());
        assertEquals(cause, e.getCause());
    }
    
}
