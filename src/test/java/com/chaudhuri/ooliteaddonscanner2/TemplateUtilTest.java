/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.File;
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
public class TemplateUtilTest {
    private static final Logger log = LogManager.getLogger();
    
    public TemplateUtilTest() {
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
     * Test of printIndex method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndex() throws Exception {
        log.info("printIndex");
    }

    /**
     * Test of printExpansions method, of class TemplateUtil.
     */
    @Test
    public void testPrintExpansions() throws Exception {
        log.info("printExpansions");
    }

    /**
     * Test of printEquipment method, of class TemplateUtil.
     */
    @Test
    public void testPrintEquipment() throws Exception {
        log.info("printEquipment");
    }

    /**
     * Test of printShips method, of class TemplateUtil.
     */
    @Test
    public void testPrintShips() throws Exception {
        log.info("printShips");
    }
    
}
