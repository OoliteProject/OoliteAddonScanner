/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
public class MainTest {
    private static final Logger log = LogManager.getLogger();
    
    public MainTest() {
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
    public void testReadToString() {
        log.info("testReadToString");
        
        {   // linefeeds ok
            
            String expected = "mytest\n";
            InputStream in = new ByteArrayInputStream(expected.getBytes());
            String read = Main.readToString(in);
            assertEquals(expected, read);
        }
        {   // linefeed missing
            
            String expected = "mytest";
            InputStream in = new ByteArrayInputStream(expected.getBytes());
            String read = Main.readToString(in);
            assertEquals(expected+"\n", read);
        }
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
    }
    
}
