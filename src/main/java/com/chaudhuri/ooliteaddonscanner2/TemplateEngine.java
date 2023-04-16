/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hiran
 */
public class TemplateEngine {
    private static final Logger log = LoggerFactory.getLogger(TemplateEngine.class);
    
    private Configuration cfg;

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
    
    public void process(Object data, String templatename, File outfile) throws IOException, TemplateException {
        log.debug("process({}, {})", data, templatename);
    
        log.info("writing {}", outfile.getAbsolutePath());
        
        Template template = cfg.getTemplate(templatename);
        Writer out = new OutputStreamWriter(new FileOutputStream(outfile));

        try {
            template.process(data, out);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not process template %s with data %s", templatename, data), e);
        }
        
        out.flush();
        out.close();
    }
}
