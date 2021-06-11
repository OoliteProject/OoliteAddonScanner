/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hiran
 */
public class Verifier {
    private static final Logger log = LoggerFactory.getLogger(Verifier.class);
    
    public static String findDiffereringPosition(String s1, String s2) {
        
        // compare the common string length
        for (int i=0; i<Math.min(s1.length(), s2.length()); i++) {
            if (s1.codePointAt(i) != s2.codePointAt(i)) {
                return String.format("at character position %04d (\\u%04X vs \\u%04X)", i+1, s1.codePointAt(i), s2.codePointAt(i));
            }
        }
        
        // no difference found in the common part
        if (s1.length() != s2.length()) {
            return "string length at character position "+Math.min(s1.length(), s2.length()+1);
        }
        
        return "no difference found";
    }
    
    /**
     *  check for licenses.
     * 
     * @param expansion 
     */
    public static void verify(Expansion expansion) {
        {   // check description
            String l1 = String.valueOf(expansion.getDescription());
            String l2 = String.valueOf(expansion.getManifest().getDescription());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Description mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }
        
        {   // check identifier
            String l1 = String.valueOf(expansion.getIdentifier());
            String l2 = String.valueOf(expansion.getManifest().getIdentifier());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Identifier mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }
        
        {   // check title
            String l1 = String.valueOf(expansion.getTitle());
            String l2 = String.valueOf(expansion.getManifest().getTitle());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Title mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }
        
        {   // check category
            String l1 = String.valueOf(expansion.getCategory());
            String l2 = String.valueOf(expansion.getManifest().getCategory());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Category mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }
        
        {   // check author
            String l1 = String.valueOf(expansion.getAuthor());
            String l2 = String.valueOf(expansion.getManifest().getAuthor());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Author mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }
        
        {   // check version
            String l1 = String.valueOf(expansion.getVersion());
            String l2 = String.valueOf(expansion.getManifest().getVersion());
            
            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Version mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }
        
        {   // check tags
            String l1 = String.valueOf(expansion.getTags());
            String l2 = String.valueOf(expansion.getManifest().getTags());
            
            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Tags mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }

        {   // check required oolite version
            String l1 = String.valueOf(expansion.getRequired_oolite_version());
            String l2 = String.valueOf(expansion.getManifest().getRequired_oolite_version());
            
            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Required Oolite Version mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }

        {   // check maximum oolite version
            String l1 = String.valueOf(expansion.getMaximum_oolite_version());
            String l2 = String.valueOf(expansion.getManifest().getMaximum_oolite_version());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Maximum Oolite Version mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }

        {   // check required expansions
            String l1 = String.valueOf(expansion.getRequires_oxps());
            String l2 = String.valueOf(expansion.getManifest().getRequires_oxps());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Required Expansions mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }

        {   // check optional expansions
            String l1 = String.valueOf(expansion.getOptional_oxps());
            String l2 = String.valueOf(expansion.getManifest().getOptional_oxps());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Optional Expansions mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }

        {   // check conflict expansions
            String l1 = String.valueOf(expansion.getConflict_oxps());
            String l2 = String.valueOf(expansion.getManifest().getConflict_oxps());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Conflict Expansions mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }

        {   // check conflict expansions
            String l1 = String.valueOf(expansion.getConflict_oxps());
            String l2 = String.valueOf(expansion.getManifest().getConflict_oxps());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Conflict Expansions mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }

        {   // check infortmation url
            String l1 = String.valueOf(expansion.getInformation_url());
            String l2 = String.valueOf(expansion.getManifest().getInformation_url());

            if (!l1.contentEquals(l2)) {
                expansion.addWarning("Information URL mismatch between OXP Manifest and Expansion Manager "+findDiffereringPosition(l1, l2));
            }
        }
        
    }
    
    public static void verify(Equipment equipment) {
        Map<String, String> features = equipment.getFeatures();
        if (features.containsKey("visible")) {
            String s = features.get("visible");
            if (!"yes".equals(s) && !"no".equals(s) && !"true".equals(s) && !"false".equals(s)) {
                equipment.addWarning("Feature visible='" + s + "' not in (yes, no, true, false) on equipment " + equipment.getIdentifier());
            }
        }
    }

    public static void verify(Ship ship) {
        if (ship.getFeatures().containsKey("is_template")) {
            String s = ship.getFeature("is_template");
            if (!"1".equals(s) && !"yes".equals(s) && !"0".equals(s) && !"no".equals(s) && !"true".equals(s) && !"false".equals(s)) {
                ship.addWarning("Feature is_template='" + s + "' not in (yes, no, true, false, 1, 0) on ship " + ship.getIdentifier());
            }
        }
    }
    
    public static void verify(Registry registry) {
        log.debug("verify({})", registry);
        
        for (Expansion expansion: registry.getExpansions()) {
            verify(expansion);
        }

        for (Equipment e: registry.getEquipment()) {
            verify(e);
        }

        for (Ship e: registry.getShips()) {
            verify(e);
        }
    }
}
