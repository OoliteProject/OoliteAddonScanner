/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import static com.chaudhuri.ooliteaddonscanner2.Main.HTML_EXTENSION;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class TemplateUtil {
    private static final Logger log = LogManager.getLogger();

    /**
     * Prevents creating instances.
     */
    private TemplateUtil() {
    }

    /**
     * Prints the generic index page plus those for expansions, ships and equipment.
     * 
     * @param registry the registry containing all the data
     * @param outputdir the directory where to store the output files
     * @param templateEngine the template engine to be used
     * @throws IOException something went wrong
     * @throws TemplateException something went wrong
     * @throws TemplateEngineException something went wrong
     */
    public static void printIndexes(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        log.debug("printIndex(...)");
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }
        if (outputdir == null) {
            throw new IllegalArgumentException("outputdir must not be null");
        }
        if (templateEngine == null) {
            throw new IllegalArgumentException("templateEngine must not be null");
        }
        
        registry.setProperty("ImplementationVendor", String.valueOf(Main.class.getPackage().getImplementationVendor()));
        registry.setProperty("ImplementationTitle", String.valueOf(Main.class.getPackage().getImplementationTitle()));
        registry.setProperty("ImplementationVersion", String.valueOf(Main.class.getPackage().getImplementationVersion()));
        templateEngine.process(registry, "index.ftlh", new File(outputdir, "index.html"));
        templateEngine.process(registry, "indexExpansionsByName.ftlh", new File(outputdir, "indexExpansionsByName.html"));
        templateEngine.process(registry, "indexEquipmentByName.ftlh", new File(outputdir, "indexEquipmentByName.html"));
        templateEngine.process(registry, "indexShipsByName.ftlh", new File(outputdir, "indexShipsByName.html"));
        templateEngine.process(registry, "indexAllByIdentifier.ftlh", new File(outputdir, "indexAllByIdentifier.html"));
        templateEngine.process(registry, "warnings.ftlh", new File(outputdir, "warnings.html"));
        templateEngine.process(registry, "style.ftlh", new File(outputdir, "style.css"));
    }
    
    /**
     * Prints one page per expansion.
     * 
     * @param registry the Registry containing all the expansions
     * @param outputdir the directory where to store the output files
     * @param templateEngine the template engine to be used
     * @throws IOException something went wrong
     * @throws TemplateException something went wrong
     * @throws TemplateEngineException something went wrong
     */
    public static void printExpansions(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }
        if (outputdir == null) {
            throw new IllegalArgumentException("outputdir must not be null");
        }
        if (templateEngine == null) {
            throw new IllegalArgumentException("templateEngine must not be null");
        }
        File expansionsDir = new File(outputdir, "expansions");
        expansionsDir.mkdirs();
        
        for (Expansion expansion: registry.getExpansions()) {
            templateEngine.process(expansion, "expansion.ftlh", new File(expansionsDir, expansion.getIdentifier()+HTML_EXTENSION));
        }
    }
    
    /**
     * Prints one page per equipment.
     * 
     * @param registry the Registry containing all the expansions
     * @param outputdir the directory where to store the output files
     * @param templateEngine the template engine to be used
     * @throws IOException something went wrong
     * @throws TemplateException something went wrong
     * @throws TemplateEngineException something went wrong
     */
    public static void printEquipment(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }
        if (outputdir == null) {
            throw new IllegalArgumentException("outputdir must not be null");
        }
        if (templateEngine == null) {
            throw new IllegalArgumentException("templateEngine must not be null");
        }
        File equipmentDir = new File(outputdir, "equipment");
        equipmentDir.mkdirs();

        for (Equipment equipment: registry.getEquipment()) {
            templateEngine.process(equipment, "equipment.ftlh", new File(equipmentDir, equipment.getIdentifier()+HTML_EXTENSION));
        }
    }
    
    /**
     * Prints one page per ship.
     * 
     * @param registry the Registry containing all the expansions
     * @param outputdir the directory where to store the output files
     * @param templateEngine the template engine to be used
     * @throws IOException something went wrong
     * @throws TemplateException something went wrong
     * @throws TemplateEngineException something went wrong
     */
    public static void printShips(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }
        if (outputdir == null) {
            throw new IllegalArgumentException("outputdir must not be null");
        }
        if (templateEngine == null) {
            throw new IllegalArgumentException("templateEngine must not be null");
        }

        for (Ship ship: registry.getShips()) {
            templateEngine.process(ship, "ship.ftlh", new File(outputdir, "ships/"+ship.getIdentifier()+HTML_EXTENSION));
        }
    }
    
}
