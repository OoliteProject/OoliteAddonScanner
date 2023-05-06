/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A template angine to process Apache FreeMarker templates.
 * 
 * @author hiran
 */
public class TemplateEngine {
    private static final Logger log = LogManager.getLogger(TemplateEngine.class);
    
    private Configuration cfg;

    /**
     * Creates a new TemplateEngine.
     */
    public TemplateEngine() {
        log.debug("TemplateEngine()");
        
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "com/chaudhuri/ooliteaddonscanner2/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }
    
    /**
     * Processes data according to a template and writes the result to file.
     * 
     * @param data the data to process
     * @param templatename the template
     * @param outfile the file to store the result in
     * @throws IOException something went wrong
     * @throws TemplateException something went wrong
     * @throws TemplateEngineException something went wrong
     */
    public void process(Object data, String templatename, File outfile) throws IOException, TemplateException, TemplateEngineException {
        log.debug("process({}, {})", data, templatename);
        if (outfile == null) {
            throw new IllegalArgumentException("outfile must not be null");
        }
        if (!outfile.getParentFile().isDirectory()) {
            throw new FileNotFoundException("Path to file does not exist: " + outfile.getAbsolutePath());
        }
    
        log.info("writing {}", outfile.getAbsolutePath());
        
        Template template = cfg.getTemplate(templatename);
        Writer out = new OutputStreamWriter(new FileOutputStream(outfile));

        try {
            template.process(data, out);
        } catch (Exception e) {
            throw new TemplateEngineException(String.format("Could not process template %s with data %s", templatename, data), e);
        }
        
        out.flush();
        out.close();
    }
}
