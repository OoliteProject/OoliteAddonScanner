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
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hiran
 */
public class Registry {
    private static final Logger log = LoggerFactory.getLogger(Registry.class);
    
    private final Map<String, Expansion> expansions;
    private final Map<String, Equipment> equipment;
    private final Map<String, Ship> ships;
    private final List<String> warnings;
    
    public Registry() {
        expansions = new TreeMap<>();
        equipment = new TreeMap<>();
        ships = new TreeMap<>();
        warnings = new ArrayList<>();
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
            log.trace("{} {}", dc.getClass(), dc.toStringTree());
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
            if ("identifier".equals(key)) {
                oxp.setIdentifier(kc.value().getText());
            } else if ("required_oolite_version".equals(key)) {
                oxp.setRequired_oolite_version(kc.value().getText());
            } else if ("title".equals(key)) {
                oxp.setTitle(kc.value().getText());
            } else if ("version".equals(key)) {
                oxp.setVersion(kc.value().getText());
            } else if ("category".equals(key)) {
                oxp.setCategory(kc.value().getText());
            } else if ("description".equals(key)) {
                oxp.setDescription(kc.value().getText());
            } else if ("download_url".equals(key)) {
                oxp.setDownload_url(kc.value().getText());
            } else if ("author".equals(key)) {
                oxp.setAuthor(kc.value().getText());
            } else if ("file_size".equals(key)) {
                oxp.setFile_size(kc.value().getText());
            } else if ("information_url".equals(key)) {
                oxp.setInformation_url(kc.value().getText());
            } else if ("license".equals(key)) {
                oxp.setLicense(kc.value().getText());
            } else if ("upload_date".equals(key)) {
                oxp.setUpload_date(kc.value().getText());
            } else if ("tags".equals(key)) {
                oxp.setTags(kc.value().getText());
            } else if ("requires_oxps".equals(key)) {
                oxp.setRequires_oxps(kc.value().getText());
            } else if ("optional_oxps".equals(key)) {
                oxp.setOptional_oxps(kc.value().getText());
            } else if ("conflict_oxps".equals(key)) {
                oxp.setConflict_oxps(kc.value().getText());
            } else if ("maximum_oolite_version".equals(key)) {
                oxp.setMaximum_oolite_version(kc.value().getText());
            } else {
                log.warn("Could not process key '{}'", key);
                //throw new IllegalArgumentException("Could not process key '" + key + "'");
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
        expansions.put(oxp.getIdentifier(), oxp);
    }
    
    /** Adds list of equipment.
     * http://wiki.alioth.net/index.php/Equipment.plist
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipmentList(Expansion expansion, List list) throws Exception {
        log.debug("addEquipmentList({}, {})", expansion, list);
        for (Object vc: list) {
            addEquipment(expansion, (List)vc);
        }
    }
    
    /** Adds list of equipment.
     * http://wiki.alioth.net/index.php/Equipment.plist
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipmentList(Expansion expansion, PlistParser.ListContext lc) {
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
    public void addEquipment(Expansion expansion, List list) throws Exception {
        log.debug("addEquipment({}, {})", expansion, list);
        Equipment equipment = new Equipment();
        equipment.setExpansion(expansion);

        try {
            equipment.setTechlevel(String.valueOf(list.get(0)));
            equipment.setCost(String.valueOf(list.get(1)));
            equipment.setName(String.valueOf(list.get(2)));
            equipment.setIdentifier(String.valueOf(list.get(3)));
            equipment.setDescription(String.valueOf(list.get(4)));

            if(list.size()>5) {
                Map<String, Object> dict = (Map<String, Object>)list.get(5);
                for (Map.Entry<String, Object> entry: dict.entrySet()) {
                    equipment.putFeature(entry.getKey(), String.valueOf(entry.getValue()));
                }
            } else {
                expansion.addWarning(String.format("No features for equipment %s", equipment.getName()));
            }            
        } catch (IndexOutOfBoundsException e) {
            throw new Exception(String.format("Could not evaluate expansion %s equipment %s", expansion, list), e);
        }
        
        // todo: can we add just like that? Do we need to check for duplicates?
        expansion.addEquipment(equipment);
        addEquipment(equipment);
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
    public void addEquipment(Expansion expansion, PlistParser.ListContext lc) {
        log.debug("addEquipment({}, {})", expansion, lc);
        Equipment equipment = new Equipment();
        equipment.setExpansion(expansion);

        equipment.setTechlevel(lc.value(0).getText());
        equipment.setCost(lc.value(1).getText());
        equipment.setName(lc.value(2).getText());
        equipment.setIdentifier(lc.value(3).getText());
        equipment.setDescription(lc.value(4).getText());
        
        try {
            PlistParser.ValueContext v = lc.value(5);
            if (v != null) {
                PlistParser.DictionaryContext d = v.dictionary();
                if (d != null) {
                    List<PlistParser.KeyvaluepairContext> kl = d.keyvaluepair();
                    if (kl != null) {
                        for (PlistParser.KeyvaluepairContext kc: kl) {
                            equipment.putFeature(kc.STRING().getText(), kc.value().getText());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not parse features for %s", equipment), e);
        }
        
        // todo: can we add just like that? Do we need to check for duplicates?
        expansion.addEquipment(equipment);
        this.equipment.put(equipment.getIdentifier(), equipment);
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
        return new ArrayList<Expansion>(expansions.values());
    }
    
    public List<Expansion> getExpansionsByName() {
        ArrayList<Expansion> result = new ArrayList<Expansion>(expansions.values());
        Collections.sort(result, new Comparator<Expansion>() {
            @Override
            public int compare(Expansion t, Expansion t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return result;
    }
    
    public List<Equipment> getEquipment() {
        return new ArrayList<Equipment>(equipment.values());
    }
    
    public List<Equipment> getEquipmentByName() {
        ArrayList<Equipment> result = new ArrayList<Equipment>(equipment.values());
        Collections.sort(result, new Comparator<Equipment>() {
            @Override
            public int compare(Equipment t, Equipment t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return result;
    }
    
    public List<Ship> getShips() {
        return new ArrayList<Ship>(ships.values());
    }
    
    public List<Ship> getShipsByName() {
        ArrayList<Ship> result = new ArrayList<Ship>(ships.values());
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
                case "category":
                    em.setCategory(String.valueOf(entry.getValue()));
                    break;
                case "conflict_oxps":
                    em.setConflict_oxps(String.valueOf(entry.getValue()));
                    break;
                case "description":
                    em.setDescription(String.valueOf(entry.getValue()));
                    break;
                case "identifier":
                    em.setIdentifier(String.valueOf(entry.getValue()));
                    break;
                case "information_url":
                    em.setInformation_url(String.valueOf(entry.getValue()));
                    break;
                case "license":
                    em.setLicense(String.valueOf(entry.getValue()));
                    break;
                case "maximum_oolite_version":
                    em.setMaximum_oolite_version(String.valueOf(entry.getValue()));
                    break;
                case "optional_oxps":
                    em.setOptional_oxps(String.valueOf(entry.getValue()));
                    break;
                case "required_oolite_version":
                    em.setRequired_oolite_version(String.valueOf(entry.getValue()));
                    break;
                case "requires_oxps":
                    em.setRequires_oxps(String.valueOf(entry.getValue()));
                    break;
                case "tags":
                    em.setTags(String.valueOf(entry.getValue()));
                    break;
                case "title":
                    em.setTitle(String.valueOf(entry.getValue()));
                    break;
                case "version":
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
            if ("identifier".equals(key)) {
                em.setIdentifier(kc.value().getText());
            } else if ("author".equals(key)) {
                em.setAuthor(kc.value().getText());
            } else if ("conflict_oxps".equals(key)) {
                em.setConflict_oxps(kc.value().getText());
            } else if ("description".equals(key)) {
                em.setDescription(kc.value().getText());
            } else if ("download_url".equals(key)) {
                em.setDownload_url(kc.value().getText());
            } else if ("category".equals(key)) {
                em.setCategory(kc.value().getText());
            } else if ("file_size".equals(key)) {
                em.setFile_size(kc.value().getText());
            } else if ("information_url".equals(key)) {
                em.setInformation_url(kc.value().getText());
            } else if ("license".equals(key)) {
                em.setLicense(kc.value().getText());
            } else if ("maximum_oolite_version".equals(key)) {
                em.setMaximum_oolite_version(kc.value().getText());
            } else if ("optional_oxps".equals(key)) {
                em.setOptional_oxps(kc.value().getText());
            } else if ("required_oolite_version".equals(key)) {
                em.setRequired_oolite_version(kc.value().getText());
            } else if ("requires_oxps".equals(key)) {
                em.setRequires_oxps(kc.value().getText());
            } else if ("tags".equals(key)) {
                em.setTags(kc.value().getText());
            } else if ("title".equals(key)) {
                em.setTitle(kc.value().getText());
            } else if ("version".equals(key)) {
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
        ArrayList<String> result = new ArrayList<String>(warnings);
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
        return new ArrayList<String>(warnings);
    }
    
}
