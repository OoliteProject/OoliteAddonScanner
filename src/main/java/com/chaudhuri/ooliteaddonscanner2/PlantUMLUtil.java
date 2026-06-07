/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Wrapper for PlantUML calls.
 * 
 * @author hiran
 */
public class PlantUMLUtil {
    private static final Logger log = LogManager.getLogger();

    /**
     * Transforms a plantuml file into a diagram.
     * 
     * @param input the input file to read
     * @param outputDir the output directory where to create the result
     * @throws IOException Something went wrong
     */
    public static void generateDiagram(File input, File outputDir) throws IOException {
        log.debug("generateDiagram({}, {})", input, outputDir);
        
        SourceFileReader reader = new SourceFileReader(false, input, outputDir.getAbsoluteFile(), new FileFormatOption(FileFormat.PNG));
        List<GeneratedImage> list = reader.getGeneratedImages();
        
        for (GeneratedImage i: list) {
            log.info("  generated {}", i);
        }
    }
}
