/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import freemarker.template.TemplateNotFoundException;
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
public class TemplateEngineTest {
    private static final Logger log = LogManager.getLogger();
    
    public TemplateEngineTest() {
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
     * Test of process method, of class TemplateEngine.
     */
    @Test
    public void testProcess() throws Exception {
        log.info("process");
        
        TemplateEngine engine = new TemplateEngine();
        File tmpDir = new File("target/test/TemplateEngineTest");
        tmpDir.mkdirs();
        File outFile = File.createTempFile("ship", ".html", tmpDir);
        outFile.delete();
        
        Expansion expansion = new Expansion();
        expansion.setIdentifier("myexpansion");
        expansion.setTitle("myexpansion-title");
        Ship data = new Ship();
        data.setIdentifier("myship");
        data.setExpansion(expansion);
        
        log.debug("writing {}", outFile);

        assertFalse(outFile.exists());
        try {
            engine.process(data, "ship", outFile);
            fail("How come the template was found?");
        } catch (TemplateNotFoundException e) {
            log.debug("caught expected exception", e);
        }
        try {
            engine.process(expansion, "ship.ftlh", outFile);
            fail("How come the template was processed?");
        } catch (TemplateEngineException e) {
            log.debug("caught expected exception", e);
        }

        engine.process(data, "ship.ftlh", outFile);
        assertTrue(outFile.exists());
    }
    
}
