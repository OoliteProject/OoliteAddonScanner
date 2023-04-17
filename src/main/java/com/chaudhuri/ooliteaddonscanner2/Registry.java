/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.PlistParser;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hiran
 */
public class Registry {
    private static final Logger log = LoggerFactory.getLogger(Registry.class);
    
    public static final String EXPANSION_CATEGORY = "category";
    public static final String EXPANSION_IDENTIFIER = "identifier";
    public static final String EXPANSION_TITLE = "title";
    public static final String EXPANSION_VERSION = "version";
    public static final String EXPANSION_REQUIRED_OOLITE_VERSION = "required_oolite_version";
    
    private final Map<String, Expansion> expansions;
    private final Map<String, Equipment> equipment;
    private final Map<String, Ship> ships;
    private final List<String> warnings;
    private final Properties properties;
    
    public Registry() {
        expansions = new TreeMap<>();
        equipment = new TreeMap<>();
        ships = new TreeMap<>();
        warnings = new ArrayList<>();
        properties = new Properties();
    }

    /** Adds a list of expansions. This list of expansions is the
     * parsed plist from the ExpansionManager.
     * 
     * @param lc the list of expansions
     */
    public void addExpansions(PlistParser.ListContext lc) {
        log.debug("addExpansions({})", lc);
        // we expect a list of dictionaries, nothing else
        
        for (PlistParser.ValueContext vc: lc.value()) {
            PlistParser.DictionaryContext dc = vc.dictionary();
            if (log.isTraceEnabled()) {
                log.trace("{} {}", dc.getClass(), dc.toStringTree());
            }
            addExpansion(dc);
        }
    }

    /** Adds a sinble expansion. The Expansion is parsed from the
     * plist from the Expansion Manager.
     * 
     * @param dc 
     */
    public void addExpansion(PlistParser.DictionaryContext dc) {
        log.debug("addExpansion({})", dc);
        
        Expansion oxp = new Expansion();
        
        for (PlistParser.KeyvaluepairContext kc: dc.keyvaluepair()) {
            log.trace("{}", kc);

            String key = kc.STRING().getText();
            if (EXPANSION_IDENTIFIER.equals(key)) {
                oxp.setIdentifier(kc.value().getText());
            } else if (EXPANSION_REQUIRED_OOLITE_VERSION.equals(key)) {
                oxp.setRequiredOoliteVersion(kc.value().getText());
            } else if (EXPANSION_TITLE.equals(key)) {
                oxp.setTitle(kc.value().getText());
            } else if (EXPANSION_VERSION.equals(key)) {
                oxp.setVersion(kc.value().getText());
            } else if (EXPANSION_CATEGORY.equals(key)) {
                oxp.setCategory(kc.value().getText());
            } else if ("description".equals(key)) {
                oxp.setDescription(kc.value().getText());
            } else if ("download_url".equals(key)) {
                oxp.setDownloadUrl(kc.value().getText());
            } else if ("author".equals(key)) {
                oxp.setAuthor(kc.value().getText());
            } else if ("file_size".equals(key)) {
                oxp.setFileSize(kc.value().getText());
            } else if ("information_url".equals(key)) {
                oxp.setInformationUrl(kc.value().getText());
            } else if ("license".equals(key)) {
                oxp.setLicense(kc.value().getText());
            } else if ("upload_date".equals(key)) {
                oxp.setUploadDate(kc.value().getText());
            } else if ("tags".equals(key)) {
                oxp.setTags(kc.value().getText());
            } else if ("requires_oxps".equals(key)) {
                oxp.setRequiresOxps(kc.value().getText());
            } else if ("optional_oxps".equals(key)) {
                oxp.setOptionalOxps(kc.value().getText());
            } else if ("conflict_oxps".equals(key)) {
                oxp.setConflictOxps(kc.value().getText());
            } else if ("maximum_oolite_version".equals(key)) {
                oxp.setMaximumOoliteVersion(kc.value().getText());
            } else {
                log.warn("Could not process key '{}'", key);
            }
        }
        
        addExpansion(oxp);
        if (oxp.getLicense() == null || oxp.getLicense().isBlank()) {
            oxp.addWarning("License not specified");
        }
    }
    
    /** Adds an expansion to the list of expansions.
     * 
     * @param oxp 
     */
    public void addExpansion(Expansion oxp) {
        if (expansions.containsKey(oxp.getIdentifier())) {
            Expansion oldOxp = expansions.get(oxp.getIdentifier());
            addWarning(String.format("OXP Overwrite! %s (%s) and %s (%s) share same id %s", oxp.getName(), oxp.getDownloadUrl(), oldOxp.getName(), oldOxp.getDownloadUrl(), oxp.getIdentifier()));
        }
        expansions.put(oxp.getIdentifier(), oxp);
    }
    
    /** Adds list of equipment.
     * http://wiki.alioth.net/index.php/Equipment.plist
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipmentList(Expansion expansion, List<List<Map<String, Object>>> list) throws RegistryException {
        log.debug("addEquipmentList({}, {})", expansion, list);
        for (List<Map<String, Object>> vc: list) {
            addEquipment(expansion, vc);
        }
    }
    
    /** Adds list of equipment.
     * http://wiki.alioth.net/index.php/Equipment.plist
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipmentList(Expansion expansion, PlistParser.ListContext lc) throws RegistryException {
        log.debug("addEquipmentList({}, {})", expansion, lc);
        if (lc==null) {
            throw new IllegalArgumentException("ListContext must not be null");
        }
        
        for (PlistParser.ValueContext vc: lc.value()) {
            addEquipment(expansion, vc.list());
        }
    }
    
    /** Adds single equipment.
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipment(Expansion expansion, List<Map<String, Object>> list) throws RegistryException {
        log.debug("addEquipment({}, {})", expansion, list);
        Equipment eq = new Equipment();
        eq.setExpansion(expansion);

        try {
            eq.setTechlevel(String.valueOf(list.get(0)));
            eq.setCost(String.valueOf(list.get(1)));
            eq.setName(String.valueOf(list.get(2)));
            eq.setIdentifier(String.valueOf(list.get(3)));
            eq.setDescription(String.valueOf(list.get(4)));

            if(list.size()>5) {
                Map<String, Object> dict = list.get(5);
                for (Map.Entry<String, Object> entry: dict.entrySet()) {
                    eq.putFeature(entry.getKey(), String.valueOf(entry.getValue()));
                }
            } else {
                expansion.addWarning(String.format("No features for equipment %s", eq.getName()));
            }            
        } catch (IndexOutOfBoundsException e) {
            throw new RegistryException(String.format("Could not evaluate expansion %s equipment %s", expansion, list), e);
        }
        
        // todo: can we add just like that? Do we need to check for duplicates?
        expansion.addEquipment(eq);
        addEquipment(eq);
    }
    
    public void addShip(Ship ship) {
        if (ships.containsKey(ship.getIdentifier())) {
            addWarning(String.format("Replacing %s/%s with %s/%s", 
                    ships.get(ship.getIdentifier()).getExpansion().getName(), 
                    ships.get(ship.getIdentifier()).getName(), 
                    ship.getExpansion().getName(), 
                    ship.getName()));
        }
        ships.put(ship.getIdentifier(), ship);
    }
    
    public void addEquipment(Equipment equipment) {
        if (this.equipment.containsKey(equipment.getIdentifier())) {
            addWarning(String.format("Replacing %s/%s with %s/%s", 
                    this.equipment.get(equipment.getIdentifier()).getExpansion().getName(), 
                    this.equipment.get(equipment.getIdentifier()).getName(), 
                    equipment.getExpansion().getName(), 
                    equipment.getName()));
        }
        this.equipment.put(equipment.getIdentifier(), equipment);
    }
    
    /** Adds single equipment.
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipment(Expansion expansion, PlistParser.ListContext lc) throws RegistryException {
        log.debug("addEquipment({}, {})", expansion, lc);
        Equipment eq = new Equipment();
        eq.setExpansion(expansion);

        eq.setTechlevel(lc.value(0).getText());
        eq.setCost(lc.value(1).getText());
        eq.setName(lc.value(2).getText());
        eq.setIdentifier(lc.value(3).getText());
        eq.setDescription(lc.value(4).getText());
        
        try {
            PlistParser.ValueContext v = lc.value(5);
            if (v != null) {
                PlistParser.DictionaryContext d = v.dictionary();
                if (d != null) {
                    List<PlistParser.KeyvaluepairContext> kl = d.keyvaluepair();
                    if (kl != null) {
                        for (PlistParser.KeyvaluepairContext kc: kl) {
                            eq.putFeature(kc.STRING().getText(), kc.value().getText());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RegistryException(String.format("Could not parse features for %s", eq), e);
        }
        
        // todo: can we add just like that? Do we need to check for duplicates?
        expansion.addEquipment(eq);
        this.equipment.put(eq.getIdentifier(), eq);
    }
    
    public void addShipList(Expansion expansion, Map<String, Object> shipList) {
        log.debug("addShipList({}, {})", expansion, shipList);

        for (Map.Entry<String, Object> entry: shipList.entrySet()) {
            addShip(expansion, entry.getKey(), (Map<String, Object>)entry.getValue());
        }
    }
    
    public void addShipList(Expansion expansion, PlistParser.DictionaryContext dc) {
        log.debug("addShipList({}, {})", expansion, dc);
        
        for (PlistParser.KeyvaluepairContext kc: dc.keyvaluepair()) {
            String key = kc.STRING().getText(); // ship identifier
            PlistParser.DictionaryContext ship = kc.value().dictionary();
            
            addShip(expansion, key, ship);
        }
    }

    public void addShip(Expansion expansion, String identifier, Map<String, Object> data) {
        log.debug("addShip({}, {}, {})", expansion, identifier, data);

        Ship ship = new Ship();
        ship.setExpansion(expansion);
        ship.setIdentifier(identifier);
        
        for (Map.Entry<String, Object> entry: data.entrySet()) {
            ship.addFeature(entry.getKey(), String.valueOf(entry.getValue()));
        }
        
        expansion.addShip(ship);
        addShip(ship);
    }
    
    public void addShip(Expansion expansion, String identifier, PlistParser.DictionaryContext dc) {
        log.debug("addShip({}, {}, {})", expansion, identifier, dc);
        
        Ship ship = new Ship();
        ship.setExpansion(expansion);
        ship.setIdentifier(identifier);
        
        for (PlistParser.KeyvaluepairContext kc: dc.keyvaluepair()) {
            ship.addFeature(kc.STRING().getText(), kc.value().getText());
        }
        
        expansion.addShip(ship);
        addShip(ship);
    }
    
    public List<Expansion> getExpansions() {
        return new ArrayList<>(expansions.values());
    }
    
    public List<Expansion> getExpansionsByName() {
        ArrayList<Expansion> result = new ArrayList<>(expansions.values());
        
        Collections.sort(result, (t, t1) -> t.getName().compareTo(t1.getName()));
        
        return result;
    }
    
    public List<Equipment> getEquipment() {
        return new ArrayList<>(equipment.values());
    }
    
    public List<Equipment> getEquipmentByName() {
        ArrayList<Equipment> result = new ArrayList<>(equipment.values());
        Collections.sort(result, new Comparator<Equipment>() {
            @Override
            public int compare(Equipment t, Equipment t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return result;
    }
    
    public List<Ship> getShips() {
        return new ArrayList<>(ships.values());
    }
    
    public List<Ship> getShipsByName() {
        ArrayList<Ship> result = new ArrayList<>(ships.values());
        Collections.sort(result, new Comparator<Ship>() {
            @Override
            public int compare(Ship t, Ship t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return result;
    }
    
    public List<Wikiworthy> getAllByIdentifier() {
        ArrayList<Wikiworthy> result = new ArrayList<>();
        result.addAll(expansions.values());
        result.addAll(equipment.values());
        result.addAll(ships.values());
        Collections.sort(result, new Comparator<Wikiworthy>() {
            @Override
            public int compare(Wikiworthy t, Wikiworthy t1) {
                return t.getIdentifier().compareTo(t1.getIdentifier());
            }
        });
        return result;
    }
    
    public ExpansionManifest toManifest(Map<String, Object> data) {
        log.debug("toManifest({})", data);
        ExpansionManifest em = new ExpansionManifest();
        
        for (Map.Entry<String, Object> entry: data.entrySet()) {
            switch (entry.getKey()) {
                case "author":
                    em.setAuthor(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_CATEGORY:
                    em.setCategory(String.valueOf(entry.getValue()));
                    break;
                case "conflict_oxps":
                    em.setConflictOxps(String.valueOf(entry.getValue()));
                    break;
                case "description":
                    em.setDescription(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_IDENTIFIER:
                    em.setIdentifier(String.valueOf(entry.getValue()));
                    break;
                case "information_url":
                    em.setInformationUrl(String.valueOf(entry.getValue()));
                    break;
                case "license":
                    em.setLicense(String.valueOf(entry.getValue()));
                    break;
                case "maximum_oolite_version":
                    em.setMaximumOoliteVersion(String.valueOf(entry.getValue()));
                    break;
                case "optional_oxps":
                    em.setOptionalOxps(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_REQUIRED_OOLITE_VERSION:
                    em.setRequiredOoliteVersion(String.valueOf(entry.getValue()));
                    break;
                case "requires_oxps":
                    em.setRequiresOxps(String.valueOf(entry.getValue()));
                    break;
                case "tags":
                    em.setTags(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_TITLE:
                    em.setTitle(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_VERSION:
                    em.setVersion(String.valueOf(entry.getValue()));
                    break;
                default:
                    em.addWarning(String.format("Unknown key '%s' in XML manifest.plist", entry.getKey()));
            }
        }
        
        return em;
    }
    
    /** Parses manifest data.
     * 
     */
    public ExpansionManifest toManifest(PlistParser.DictionaryContext dc) {
        log.debug("toManifest({})", dc);
        ExpansionManifest em = new ExpansionManifest();
        
        for (PlistParser.KeyvaluepairContext kc: dc.keyvaluepair()) {
            String key = kc.STRING().getText();
            if (EXPANSION_IDENTIFIER.equals(key)) {
                em.setIdentifier(kc.value().getText());
            } else if ("author".equals(key)) {
                em.setAuthor(kc.value().getText());
            } else if ("conflict_oxps".equals(key)) {
                em.setConflictOxps(kc.value().getText());
            } else if ("description".equals(key)) {
                em.setDescription(kc.value().getText());
            } else if ("download_url".equals(key)) {
                em.setDownloadUrl(kc.value().getText());
            } else if (EXPANSION_CATEGORY.equals(key)) {
                em.setCategory(kc.value().getText());
            } else if ("file_size".equals(key)) {
                em.setFileSize(kc.value().getText());
            } else if ("information_url".equals(key)) {
                em.setInformationUrl(kc.value().getText());
            } else if ("license".equals(key)) {
                em.setLicense(kc.value().getText());
            } else if ("maximum_oolite_version".equals(key)) {
                em.setMaximumOoliteVersion(kc.value().getText());
            } else if ("optional_oxps".equals(key)) {
                em.setOptionalOxps(kc.value().getText());
            } else if (EXPANSION_REQUIRED_OOLITE_VERSION.equals(key)) {
                em.setRequiredOoliteVersion(kc.value().getText());
            } else if ("requires_oxps".equals(key)) {
                em.setRequiresOxps(kc.value().getText());
            } else if ("tags".equals(key)) {
                em.setTags(kc.value().getText());
            } else if (EXPANSION_TITLE.equals(key)) {
                em.setTitle(kc.value().getText());
            } else if (EXPANSION_VERSION.equals(key)) {
                em.setVersion(kc.value().getText());
            } else {
                log.trace("Unknown key {}->{} at {}", key, kc.value().getText(), kc.getStart().getTokenSource().getSourceName());
                em.addWarning(String.format("Unknown key '%s' at %s", key, kc.getStart().getTokenSource().getSourceName()));
            }
        }
        return em;
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    /** Returns global warnings and all the others.
     * 
     * @return 
     */
    public List<String> getWarnings() {
        ArrayList<String> result = new ArrayList<>(warnings);
        for (Expansion e: expansions.values()) {
            result.addAll(e.getWarnings());
        }
        return result;
    }
    
    /** Returns global warnings only.
     * 
     * @return 
     */
    public List<String> getGlobalWarnings() {
        return new ArrayList<>(warnings);
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    public Properties getProperties() {
        return properties;
    }
    
}
