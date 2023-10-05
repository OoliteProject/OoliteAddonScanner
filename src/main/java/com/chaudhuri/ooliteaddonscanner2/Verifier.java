/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import java.lang.module.ModuleDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class Verifier {
    private static final Logger log = LogManager.getLogger(Verifier.class);
    
    /**
     * Prevent this class from being instantiated.
     */
    private Verifier() {
    }
    
    /**
     * Return a human readable name for a character codepoint.
     * 
     * @param codepoint the codepoint
     * @return the character name
     */
    public static String describeCodePoint(int codepoint) {
        String s = Character.getName(codepoint);
        if (s != null) {
            return s;
        }
        return String.format("\\\\u%04X", codepoint);
    }

    /**
     * Compares the two strings and returns a desription of where discrepancy
     * is found.
     * 
     * @param s1 one string to compare
     * @param s2 the other string to compare
     * @return the descriptive string of difference
     */
    public static String findDiffereringPosition(String s1, String s2) {
        if (s1 == null) {
            throw new IllegalArgumentException("s1 must not be null");
        }
        if (s2 == null) {
            return "compare with undefined value";
        }
        
        // compare the common string length
        for (int i=0; i<Math.min(s1.length(), s2.length()); i++) {
            if (s1.codePointAt(i) != s2.codePointAt(i)) {
                return String.format("at character position %04d (%s vs %s)", i+1, describeCodePoint(s1.codePointAt(i)), describeCodePoint(s2.codePointAt(i)));
            }
        }
        
        // no difference found in the common part
        if (s1.length() != s2.length()) {
            return "string length at character position "+Math.min(s1.length(), s2.length()+1);
        }
        
        return "no difference found";
    }

    private static void verifyExpansion1(Expansion expansion) {   // check description
        String l1 = String.valueOf(expansion.getDescription());
        String l2 = String.valueOf(expansion.getManifest().getDescription());

        // strip off whitespace
        String s1 = l1.trim().replace('\t', ' ').replace('\r', ' ').replace('\n', ' ').replace("  ", " ");
        String s2 = l2.trim().replace('\t', ' ').replace('\r', ' ').replace('\n', ' ').replace("  ", " ");

        if (!s1.contentEquals(s2)) {
            expansion.addWarning("Description mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }

    private static void verifyExpansion2(Expansion expansion) {   // check identifier
        String l1 = String.valueOf(expansion.getIdentifier());
        String l2 = String.valueOf(expansion.getManifest().getIdentifier());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Identifier mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }
    
    private static void verifyExpansion3(Expansion expansion) {   // check title
        String l1 = String.valueOf(expansion.getTitle());
        String l2 = String.valueOf(expansion.getManifest().getTitle());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Title mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }

    private static void verifyExpansion4(Expansion expansion) {   // check category
        String l1 = String.valueOf(expansion.getCategory());
        String l2 = String.valueOf(expansion.getManifest().getCategory());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Category mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }

    private static void verifyExpansion5(Expansion expansion) {   // check author
        String l1 = String.valueOf(expansion.getAuthor());
        String l2 = String.valueOf(expansion.getManifest().getAuthor());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Author mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }

    private static void verifyExpansion6(Expansion expansion) {   // check version
        String l1 = String.valueOf(expansion.getVersion());
        String l2 = String.valueOf(expansion.getManifest().getVersion());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Version mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }

    private static void verifyExpansion7(Expansion expansion) {   // check tags
        String l1 = expansion.getTags();
        String l2 = expansion.getManifest().getTags();

        if (needsWarning(l1, l2)) {
            expansion.addWarning("Tags mismatch between OXP Manifest and Expansion Manager "
                    +findDiffereringPosition(l1, l2)
                    +"('" + l1 + "' vs '" + l2 + "')");
        }
    }

    private static void verifyExpansion8(Expansion expansion) {   // check required oolite version
        String l1 = String.valueOf(expansion.getRequiredOoliteVersion());
        String l2 = String.valueOf(expansion.getManifest().getRequiredOoliteVersion());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Required Oolite Version mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }
    
    private static boolean needsWarning(String l1, String l2) {
        if (l1 == null) {
            if (l2 == null) {
                return false;
            }
            if (l2.isBlank()) {
                return false;
            }
            
            return true;
        } else {
            if (l2 == null) {
                if (l1.isBlank()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return !l1.contentEquals(l2);
            }
        }
    }

    private static void verifyExpansion9(Expansion expansion) {   // check maximum oolite version
        String l1 = expansion.getMaximumOoliteVersion();
        String l2 = expansion.getManifest().getMaximumOoliteVersion();

        if (needsWarning(l1, l2)) {
            expansion.addWarning("Maximum Oolite Version mismatch between OXP Manifest and Expansion Manager "
                    +findDiffereringPosition(l1, l2)
                    +"('" + l1 + "' vs '" + l2 + "')");
        }
    }

    private static void verifyExpansion10(Expansion expansion) {   // check required expansions
        String l1 = String.valueOf(expansion.getRequiresOxps());
        String l2 = String.valueOf(expansion.getManifest().getRequiresOxps());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Required Expansions mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }

    private static void verifyExpansion11(Expansion expansion) {   // check optional expansions
        String l1 = String.valueOf(expansion.getOptionalOxps());
        String l2 = String.valueOf(expansion.getManifest().getOptionalOxps());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Optional Expansions mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }

    private static void verifyExpansion12(Expansion expansion) {   // check conflict expansions
        String l1 = String.valueOf(expansion.getConflictOxps());
        String l2 = String.valueOf(expansion.getManifest().getConflictOxps());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Conflict Expansions mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }
    
    private static void verifyExpansion13(Expansion expansion) {   // check infortmation url
        String l1 = String.valueOf(expansion.getInformationUrl());
        String l2 = String.valueOf(expansion.getManifest().getInformationUrl());

        if (!l1.contentEquals(l2)) {
            expansion.addWarning("Information URL mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
        }
    }
    
    private static void verifyExpansion14(Expansion expansion) {
        Pattern pFetch = Pattern.compile("\\sfetch\\s");
        Pattern purl = Pattern.compile("\\surl\\s");
        Pattern pxmlhttprequest = Pattern.compile("\\sxmlhttprequest\\s");

        for (String rawscript: expansion.getScripts().values()) {
            String script = rawscript.toLowerCase();
            if (pFetch.matcher(script).matches()) {
                expansion.addWarning("JavaScript with fetch");
            }
            if (purl.matcher(script).matches()) {
                expansion.addWarning("JavaScript with url");
            }
            if (pxmlhttprequest.matcher(script).matches()) {
                expansion.addWarning("JavaScript with xmlhttprequest");
            }
        }
    }
    
    /**
     * Checks whether the dependencies can be resolved within the registry.
     * Applies warnings to the expansion.
     * See https://wiki.alioth.net/index.php/Manifest.plist#Dependency_management_keys
     * 
     * @param dependencies
     * @param expansion
     * @param registry 
     */
    protected static void verifyDependency(Expansion.Dependency dependency, Expansion expansion, Registry registry) {
        if (dependency == null) {
            throw new IllegalArgumentException("dependency must not be null");
        }
        if (expansion == null) {
            throw new IllegalArgumentException("expansion must not be null");
        }
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }

        if (dependency.getVersion() == null) {
            expansion.addWarning(String.format("No version in dependency reference to %s:%s", dependency.getIdentifier(), dependency.getVersion()));
            return;
        }
        
        List<Expansion> candidates = new ArrayList<>(registry.getExpansions());
        
        // remove everything that does not match the identifier
        List<Expansion> toBeRemoved = new ArrayList<>();
        for (Expansion ex: candidates) {
            if (!ex.getIdentifier().equals(dependency.getIdentifier())) {
                toBeRemoved.add(ex);
            }
        }
        candidates.removeAll(toBeRemoved);

        if (candidates.isEmpty()) {
            expansion.addWarning(String.format("Unresolved dependency reference to %s:%s", dependency.getIdentifier(), dependency.getVersion()));
            return;
        }
        
        if ("0".equals(dependency.getVersion())) {
            // we found at least one match. done
            return;
        }
        
        // remove everything that does not match the version
        try {
            ModuleDescriptor.Version minVersion = ModuleDescriptor.Version.parse(dependency.getVersion());
            toBeRemoved = new ArrayList<>();
            for (Expansion ex: candidates) {
                ModuleDescriptor.Version exVersion = ModuleDescriptor.Version.parse(ex.getVersion());
                if (minVersion.compareTo(exVersion) > 0) {
                    toBeRemoved.add(ex);
                }
            }
            candidates.removeAll(toBeRemoved);

            if (candidates.isEmpty()) {
                expansion.addWarning(String.format("Unresolved dependency reference to %s:%s", dependency.getIdentifier(), dependency.getVersion()));
            }
        } catch (Exception e) {
            expansion.addWarning(String.format("Could not compare dependency version number to %s:%s", dependency.getIdentifier(), dependency.getVersion()));
        }
    }
    
    /**
     * Checks whether the dependencies can be resolved within the registry.
     * Applies warnings to the expansion.
     * See https://wiki.alioth.net/index.php/Manifest.plist#Dependency_management_keys
     * 
     * @param dependencies
     * @param expansion
     * @param registry 
     */
    private static void verifyDependencies(List<Expansion.Dependency> dependencies, Expansion expansion, Registry registry) {
        if (dependencies == null) {
            throw new IllegalArgumentException("dependencies must not be null");
        }
        for (Expansion.Dependency dependency: dependencies) {
            verifyDependency(dependency, expansion, registry);
        }
    }
    
    /**
     * Verifies an expansion and adds warnings to the expansion.
     * 
     * @param expansion 
     * @param registry
     */
    public static void verify(Expansion expansion, Registry registry) {
        verifyExpansion1(expansion);
        verifyExpansion2(expansion);
        verifyExpansion3(expansion);
        verifyExpansion4(expansion);
        verifyExpansion5(expansion);
        verifyExpansion6(expansion);
        verifyExpansion7(expansion);
        verifyExpansion8(expansion);
        verifyExpansion9(expansion);
        verifyExpansion10(expansion);
        verifyExpansion11(expansion);
        verifyExpansion12(expansion);
        verifyExpansion13(expansion);
        verifyExpansion14(expansion);
        verifyDependencies(expansion.getRequiresOxps(), expansion, registry);
        verifyDependencies(expansion.getOptionalOxps(), expansion, registry);
    }
    
    /**
     * Verifies some equipment.
     * Found problems are attached as warnings to the equipment.
     * 
     * @param equipment the equipment to verify
     */
    public static void verify(Equipment equipment) {
        Map<String, String> features = equipment.getFeatures();
        if (features.containsKey("visible")) {
            String s = features.get("visible");
            if (!"yes".equals(s) && !"no".equals(s) && !"true".equals(s) && !"false".equals(s)) {
                equipment.addWarning("Feature visible='" + s + "' not in (yes, no, true, false) on equipment " + equipment.getIdentifier());
            }
        }
    }

    /**
     * Verifies a ship.
     * Found problems are attached as warnings to the ship.
     * 
     * @param ship the ship to verify
     */
    public static void verify(Ship ship) {
        if (ship.getFeatures().containsKey("is_template")) {
            String s = ship.getFeature("is_template");
            if (!"1".equals(s) && !"yes".equals(s) && !"0".equals(s) && !"no".equals(s) && !"true".equals(s) && !"false".equals(s)) {
                ship.addWarning("Feature is_template='" + s + "' not in (yes, no, true, false, 1, 0) on ship " + ship.getIdentifier());
            }
        }
    }

    /**
     * Verifies a registry.
     * Found problems are attached as warnings to the objects inside.
     * 
     * @param registry the registry
     */
    public static void verify(Registry registry) {
        log.debug("verify({})", registry);
        
        for (Expansion expansion: registry.getExpansions()) {
            verify(expansion, registry);
        }

        for (Equipment e: registry.getEquipment()) {
            verify(e);
        }

        for (Ship e: registry.getShips()) {
            verify(e);
        }
    }
}
