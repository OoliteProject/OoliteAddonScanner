/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.plist.PlistParserUtil;
import com.chaudhuri.plist.PlistParser;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Warnable;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class Registry {
    private static final Logger log = LogManager.getLogger(Registry.class);

    private static final String EXCEPTION_EXPANSION_MUST_NOT_BE_NULL = "expansion must not be null";
    public static final String EXPANSION_AUTHOR = "author";
    public static final String EXPANSION_CATEGORY = "category";
    public static final String EXPANSION_CONFLICT_OXPS = "conflict_oxps";
    public static final String EXPANSION_DESCRIPTION = "description";
    public static final String EXPANSION_IDENTIFIER = "identifier";
    public static final String EXPANSION_INFORMATION_URL = "information_url";
    public static final String EXPANSION_LICENSE = "license";
    public static final String EXPANSION_MAXIMUM_OOLITE_VERSION = "maximum_oolite_version";
    public static final String EXPANSION_OPTIONAL_OXPS = "optional_oxps";
    public static final String EXPANSION_REQUIRES_OXPS = "requires_oxps";
    public static final String EXPANSION_TITLE = "title";
    public static final String EXPANSION_TAGS = "tags";
    public static final String EXPANSION_VERSION = "version";
    public static final String EXPANSION_REQUIRED_OOLITE_VERSION = "required_oolite_version";
    
    private final Map<String, Expansion> expansions;
    private final Map<String, Equipment> equipment;
    private final Map<String, Ship> ships;
    private final List<String> warnings;
    private final Properties properties;
    
    /**
     * Creates a new Registry.
     */
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
        
        if (lc == null) {
            throw new IllegalArgumentException("lc must not be null");
        }
        
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

            evaluateOxpKeys(kc, oxp);
        }
        
        addExpansion(oxp);
        if (oxp.getLicense() == null || oxp.getLicense().isBlank()) {
            oxp.addWarning("License not specified");
        }
    }
    
    /**
     * Removes an expansion from this registry.
     * 
     * @param toRemove the expansion to be removed
     */
    public void removeExpansion(Expansion toRemove) {
        expansions.remove(toRemove.getIdentifier());
    }
    
    /**
     * Removes several expansions from this registry.
     * 
     * @param toRemove the list of expansions to be removed
     */
    public void removeExpansions(List<Expansion> toRemove) {
        for (Expansion e: toRemove) {
            removeExpansion(e);
        }
    }
    
    private static void evaluateOxpKeys(PlistParser.KeyvaluepairContext kc, Expansion oxp) {

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
        } else if (EXPANSION_DESCRIPTION.equals(key)) {
            oxp.setDescription(kc.value().getText());
        } else if ("download_url".equals(key)) {
            oxp.setDownloadUrl(kc.value().getText());
        } else if (EXPANSION_AUTHOR.equals(key)) {
            oxp.setAuthor(kc.value().getText());
        } else if ("file_size".equals(key)) {
            oxp.setFileSize(kc.value().getText());
        } else if (EXPANSION_INFORMATION_URL.equals(key)) {
            oxp.setInformationUrl(kc.value().getText());
        } else {
            evaluateOxpKeys2(kc, oxp);
        }
    }
    
    private static List<Expansion.Dependency> parseDependencies(PlistParser.ValueContext vc, Warnable warnable) {
        List<Expansion.Dependency> result = new ArrayList<>();
        
        if (vc.list() != null) {
            for (PlistParser.ValueContext vc2: vc.list().value()) {
                PlistParser.DictionaryContext dict = vc2.dictionary();
                
                Expansion.Dependency dependency = new Expansion.Dependency();

                for (PlistParser.KeyvaluepairContext kvc: dict.keyvaluepair()) {
                    String key = kvc.STRING().getText();
                    String value = kvc.value().getText();
                    switch(key) {
                        case EXPANSION_IDENTIFIER:
                            dependency.setIdentifier(value);
                            break;
                        case EXPANSION_VERSION:
                            dependency.setVersion(value);
                            break;
                        case EXPANSION_DESCRIPTION:
                            dependency.setDescription(value);
                            break;
                        case "maximum_version":
                            dependency.setMaxVersion(value);
                            break;
                        default:
                            log.warn("unknown dependency key {}", key);
                            warnable.addWarning("Dependency with illegal key '" + key + "'");
                            break;
                    }
                    
                }
                result.add(dependency);
            }
        }
        
        return result;
    }

    private static void evaluateOxpKeys2(PlistParser.KeyvaluepairContext kc, Expansion oxp) {
        String key = kc.STRING().getText();
        if (EXPANSION_LICENSE.equals(key)) {
            oxp.setLicense(kc.value().getText());
        } else if ("upload_date".equals(key)) {
            oxp.setUploadDate(kc.value().getText());
        } else if (EXPANSION_TAGS.equals(key)) {
            oxp.setTags(new ArrayList<>(PlistParserUtil.getStringList(kc.value())));
        } else if (EXPANSION_REQUIRES_OXPS.equals(key)) {
            oxp.setRequiresOxps(parseDependencies(kc.value(), oxp));
        } else if (EXPANSION_OPTIONAL_OXPS.equals(key)) {
            oxp.setOptionalOxps(parseDependencies(kc.value(), oxp));
        } else if (EXPANSION_CONFLICT_OXPS.equals(key)) {
            oxp.setConflictOxps(parseDependencies(kc.value(), oxp));
        } else if (EXPANSION_MAXIMUM_OOLITE_VERSION.equals(key)) {
            oxp.setMaximumOoliteVersion(kc.value().getText());
        } else {
            log.warn("Could not process key '{}'", key);
        }
    }
    
    private static void evaluateOxpKeys(PlistParser.KeyvaluepairContext kc, ExpansionManifest em) {
        String key = kc.STRING().getText();
        
        if (EXPANSION_IDENTIFIER.equals(key)) {
            em.setIdentifier(kc.value().getText());
        } else if (EXPANSION_AUTHOR.equals(key)) {
            em.setAuthor(kc.value().getText());
        } else if (EXPANSION_CONFLICT_OXPS.equals(key)) {
            em.setConflictOxps(parseDependencies(kc.value(), em));
        } else if (EXPANSION_DESCRIPTION.equals(key)) {
            em.setDescription(kc.value().getText());
        } else if ("download_url".equals(key)) {
            em.setDownloadUrl(kc.value().getText());
        } else if (EXPANSION_CATEGORY.equals(key)) {
            em.setCategory(kc.value().getText());
        } else if ("file_size".equals(key)) {
            em.setFileSize(kc.value().getText());
        } else {
            evaluateOxpKeys2(kc, em);
        }
    }
    
    private static void evaluateOxpKeys2(PlistParser.KeyvaluepairContext kc, ExpansionManifest em) {
        String key = kc.STRING().getText();
        
        if (EXPANSION_INFORMATION_URL.equals(key)) {
            em.setInformationUrl(kc.value().getText());
        } else if (EXPANSION_LICENSE.equals(key)) {
            em.setLicense(kc.value().getText());
        } else if (EXPANSION_MAXIMUM_OOLITE_VERSION.equals(key)) {
            em.setMaximumOoliteVersion(kc.value().getText());
        } else if (EXPANSION_OPTIONAL_OXPS.equals(key)) {
            em.setOptionalOxps(parseDependencies(kc.value(), em));
        } else if (EXPANSION_REQUIRED_OOLITE_VERSION.equals(key)) {
            em.setRequiredOoliteVersion(kc.value().getText());
        } else if (EXPANSION_REQUIRES_OXPS.equals(key)) {
            em.setRequiresOxps(parseDependencies(kc.value(), em));
        } else if (EXPANSION_TAGS.equals(key)) {
            em.setTags(PlistParserUtil.getStringList(kc.value()));
        } else if (EXPANSION_TITLE.equals(key)) {
            em.setTitle(kc.value().getText());
        } else if (EXPANSION_VERSION.equals(key)) {
            em.setVersion(kc.value().getText());
        } else {
            log.trace("Unknown key {}->{} at {}", key, kc.value().getText(), kc.getStart().getTokenSource().getSourceName());
            em.addWarning(String.format("Unknown key '%s' at %s", key, kc.getStart().getTokenSource().getSourceName()));
        }
    }
    
    /** Adds an expansion to the list of expansions.
     * 
     * @param expansion 
     */
    public void addExpansion(Expansion expansion) {
        if (expansion.getIdentifier() == null) {
            throw new IllegalArgumentException("expansion identifier must not be null");
        }
        
        if (expansions.containsKey(expansion.getIdentifier())) {
            Expansion oldOxp = expansions.get(expansion.getIdentifier());
            addWarning(String.format("OXP Overwrite! %s (%s) and %s (%s) share same id %s", expansion.getName(), expansion.getDownloadUrl(), oldOxp.getName(), oldOxp.getDownloadUrl(), expansion.getIdentifier()));
        }
        expansions.put(expansion.getIdentifier(), expansion);
    }
    
    /**
     * Returns the matching expansion or null.
     * 
     * The "identifier" must match the "identifier" of the other OXP as set in 
     * its manifest.plist.
     * 
     * See https://wiki.alioth.net/index.php/Manifest.plist#Dependency_management_keys
     * 
     * @param identifier the identifier
     * @return the expansion, or null if not found
     */
    public Expansion getExpansion(String identifier) {
        return getExpansion(identifier, null);
    }
    
    /**
     * Returns the matching expansion or null.
     * 
     * The "identifier" must match the "identifier" of the other OXP as set in 
     * its manifest.plist, and the "version" is the minimum version of that OXP
     * ("0" will match all versions). 
     * 
     * See https://wiki.alioth.net/index.php/Manifest.plist#Dependency_management_keys
     * 
     * @param identifier the identifier
     * @param version the version
     * @return the expansion, or null if not found
     */
    public Expansion getExpansion(String identifier, String version) {
        return expansions.get(identifier);
    }
    
    /** Adds list of equipment.
     * http://wiki.alioth.net/index.php/Equipment.plist
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipmentList(Expansion expansion, List<List<Object>> list) throws RegistryException {
        log.debug("addEquipmentList({}, {})", expansion, list);
        if (expansion==null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (list==null) {
            throw new IllegalArgumentException("list must not be null");
        }
        
        for (List<Object> vc: list) {
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
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (lc==null) {
            throw new IllegalArgumentException("ListContext must not be null");
        }
        
        // for each equipment
        for (PlistParser.ValueContext vc: lc.value()) {
            
            // the equipment is a list by itself
            log.trace("ValueContext: {}", vc.getText());
            if (vc.list() != null) {
                addEquipment(expansion, vc.list());
            }
        }
    }
    
    /** Adds single equipment.
     * 
     * @param expansion
     * @param lc 
     */
    public void addEquipment(Expansion expansion, List<Object> list) throws RegistryException {
        log.debug("addEquipment({}, {})", expansion, list);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (list == null) {
            throw new IllegalArgumentException("list must not be null");
        }
        
        Equipment eq = new Equipment();
        eq.setExpansion(expansion);

        try {
            eq.setTechlevel(String.valueOf(list.get(0)));
            eq.setCost(String.valueOf(list.get(1)));
            eq.setName(String.valueOf(list.get(2)));
            eq.setIdentifier(String.valueOf(list.get(3)));
            eq.setDescription(String.valueOf(list.get(4)));

            if(list.size()>5) {
                Map<String, Object> dict = (Map<String, Object>)list.get(5);
                for (Map.Entry<String, Object> entry: dict.entrySet()) {
                    eq.putFeature(entry.getKey(), String.valueOf(entry.getValue()));
                }
            } else {
                expansion.addWarning(String.format("No features for equipment %s", eq.getName()));
            }            
        } catch (IndexOutOfBoundsException e) {
            throw new RegistryException(String.format("Could not evaluate expansion %s equipment %s", expansion, list), e);
        }
        
        expansion.addEquipment(eq);
        addEquipment(eq);
    }
    
    /**
     * Adds a ship.
     * 
     * @param ship the ship to add
     */
    public void addShip(Ship ship) {
        if (ship.getIdentifier() == null) {
            throw new IllegalArgumentException("Ship must not have null identifier");
        }
        if (ship.getExpansion() == null) {
            throw new IllegalArgumentException("Ship must have expansion reference");
        }
        
        if (ships.containsKey(ship.getIdentifier())) {
            addWarning(String.format("Replacing %s/%s with %s/%s", 
                    ships.get(ship.getIdentifier()).getExpansion().getName(), 
                    ships.get(ship.getIdentifier()).getName(), 
                    ship.getExpansion().getName(), 
                    ship.getName()));
        }
        
        ships.put(ship.getIdentifier(), ship);
    }
    
    /**
     * Adds equipment.
     * 
     * @param equipment the equipment
     */
    public void addEquipment(Equipment equipment) {
        if (equipment.getIdentifier() == null) {
            throw new IllegalArgumentException("equipment must have identifier");
        }
        if (equipment.getExpansion() == null) {
            throw new IllegalArgumentException("equipment must have expansion");
        }
        if (this.equipment.containsKey(equipment.getIdentifier())) {
            addWarning(String.format("Replacing %s/%s with %s/%s", 
                    this.equipment.get(equipment.getIdentifier()).getExpansion().getName(), 
                    this.equipment.get(equipment.getIdentifier()).getName(), 
                    equipment.getExpansion().getName(), 
                    equipment.getName()));
        }
        this.equipment.put(equipment.getIdentifier(), equipment);
    }
    
    /** 
     * Adds single equipment.
     * 
     * @param expansion the expansion the equipment is part of
     * @param lc the parser context from which to read the equipment
     */
    public void addEquipment(Expansion expansion, PlistParser.ListContext lc) throws RegistryException {
        log.debug("addEquipment({}, {})", expansion, lc);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (lc == null) {
            throw new IllegalArgumentException("lc must not be null");
        }
        
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
        
        expansion.addEquipment(eq);
        this.equipment.put(eq.getIdentifier(), eq);
    }
    
    /**
     * Adds a list of ships.
     * 
     * @param expansion the expansion the ahip will belong to
     * @param shipList the list from which to add the ships
     */
    public void addShipList(Expansion expansion, Map<String, Object> shipList) {
        log.debug("addShipList({}, {})", expansion, shipList);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (shipList == null) {
            throw new IllegalArgumentException("shipList must not be null");
        }

        for (Map.Entry<String, Object> entry: shipList.entrySet()) {
            addShip(expansion, entry.getKey(), (Map<String, Object>)entry.getValue());
        }
    }
    
    /**
     * Adds a list of ships.
     * 
     * @param expansion the expansion the ahip will belong to
     * @param dc the dictionary context to read the ships from
     */
    public void addShipList(Expansion expansion, PlistParser.DictionaryContext dc) {
        log.debug("addShipList({}, {})", expansion, dc);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (dc == null) {
            throw new IllegalArgumentException("dc must not be null");
        }
        
        for (PlistParser.KeyvaluepairContext kc: dc.keyvaluepair()) {
            String key = kc.STRING().getText(); // ship identifier
            PlistParser.DictionaryContext ship = kc.value().dictionary();
            
            addShip(expansion, key, ship);
        }
    }

    /**
     * Adds a list of ships.
     * 
     * @param expansion the expansion the ahip will belong to
     * @param data a map holding the ships
     */
    public void addShip(Expansion expansion, String identifier, Map<String, Object> data) {
        log.debug("addShip({}, {}, {})", expansion, identifier, data);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (identifier == null) {
            throw new IllegalArgumentException("identifier must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        Ship ship = new Ship();
        ship.setExpansion(expansion);
        ship.setIdentifier(identifier);
        
        for (Map.Entry<String, Object> entry: data.entrySet()) {
            ship.addFeature(entry.getKey(), String.valueOf(entry.getValue()));
        }
        
        expansion.addShip(ship);
        addShip(ship);
    }
    
    /**
     * Adds a list of ships.
     * 
     * @param expansion the expansion the ships will belong to
     * @param dc the dictionary context to read the ships from
     */
    public void addShip(Expansion expansion, String identifier, PlistParser.DictionaryContext dc) {
        log.debug("addShip({}, {}, {})", expansion, identifier, dc);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (identifier == null) {
            throw new IllegalArgumentException("identifier must not be null");
        }
        if (dc == null) {
            throw new IllegalArgumentException("dc must not be null");
        }
        
        Ship ship = new Ship();
        ship.setExpansion(expansion);
        ship.setIdentifier(identifier);
        
        for (PlistParser.KeyvaluepairContext kc: dc.keyvaluepair()) {
            ship.addFeature(kc.STRING().getText(), kc.value().getText());
        }
        
        expansion.addShip(ship);
        addShip(ship);
    }
    
    /**
     * Returns the list of expansion.
     * 
     * @return the list
     */
    public List<Expansion> getExpansions() {
        return new ArrayList<>(expansions.values());
    }

    /**
     * Returns the list of expansions sorted by name.
     * @return the list
     */
    public List<Expansion> getExpansionsByName() {
        ArrayList<Expansion> result = new ArrayList<>(expansions.values());
        
        Collections.sort(result, (t, t1) -> t.getName().compareTo(t1.getName()));
        
        return result;
    }

    /**
     * Returns the list of expansions sorted by upload date.
     * @return the list
     */
    public List<Expansion> getExpansionsByUploadDate() {
        ArrayList<Expansion> result = new ArrayList<>(expansions.values());
       
        Collections.sort(result, (t, t1) -> {
            if (t==null && t1==null) {
                return 0;
            }
            if (t==null) {
                return -1;
            }
            if (t.getUploadDate()==null) {
                return -1;
            }

            Instant i = t.getUploadDateAsInstant();
            Instant i1 = t1.getUploadDateAsInstant();
            return i.compareTo(i1);
        });
        
        return result;
    }

    /**
     * Returns the list of equipment.
     * 
     * @return the list
     */
    public List<Equipment> getEquipment() {
        return new ArrayList<>(equipment.values());
    }
    
    /**
     * Returns the list of equipment, sorted by name.
     * 
     * @return the list
     */
    public List<Equipment> getEquipmentByName() {
        ArrayList<Equipment> result = new ArrayList<>(equipment.values());
        Collections.sort(result, (t, t1) -> t.getName().compareTo(t1.getName()));
        return result;
    }

    /**
     * Returns the list of ships.
     * 
     * @return the list
     */
    public List<Ship> getShips() {
        return new ArrayList<>(ships.values());
    }

    /**
     * Returns the list of ships, sorted by name.
     * 
     * @return the list
     */
    public List<Ship> getShipsByName() {
        ArrayList<Ship> result = new ArrayList<>(ships.values());
        Collections.sort(result, (t, t1) -> t.getName().compareTo(t1.getName()));
        return result;
    }

    /**
     * Returns all ships, equipment, expansions, sorted by identifier.
     * 
     * @return the list
     */
    public List<Wikiworthy> getAllByIdentifier() {
        ArrayList<Wikiworthy> result = new ArrayList<>();
        result.addAll(expansions.values());
        result.addAll(equipment.values());
        result.addAll(ships.values());
        
        Collections.sort(result, (t, t1) -> t.getIdentifier().compareTo(t1.getIdentifier()));
        
        return result;
    }

    /**
     * Parses manifest data.
     * 
     * @param data the data to read
     * @return the ExpansionManifest
     */
    public ExpansionManifest toManifest(Map<String, Object> data) {
        log.debug("toManifest({})", data);
        ExpansionManifest em = new ExpansionManifest();
        
        for (Map.Entry<String, Object> entry: data.entrySet()) {
            switch (entry.getKey()) {
                case EXPANSION_AUTHOR:
                    em.setAuthor(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_CATEGORY:
                    em.setCategory(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_CONFLICT_OXPS:
                    em.setConflictOxps(parseDependencies((PlistParser.ValueContext)entry.getValue(), em));
                    break;
                case EXPANSION_DESCRIPTION:
                    em.setDescription(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_IDENTIFIER:
                    em.setIdentifier(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_INFORMATION_URL:
                    em.setInformationUrl(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_LICENSE:
                    em.setLicense(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_MAXIMUM_OOLITE_VERSION:
                    em.setMaximumOoliteVersion(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_OPTIONAL_OXPS:
                    em.setOptionalOxps(parseDependencies((PlistParser.ValueContext)entry.getValue(), em));
                    break;
                case EXPANSION_REQUIRED_OOLITE_VERSION:
                    em.setRequiredOoliteVersion(String.valueOf(entry.getValue()));
                    break;
                case EXPANSION_REQUIRES_OXPS:
                    em.setRequiresOxps(parseDependencies((PlistParser.ValueContext)entry.getValue(), em));
                    break;
                case EXPANSION_TAGS:
                    em.setTags(new ArrayList<String>((List<String>)entry.getValue()));
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
    
    /** 
     * Parses manifest data.
     * 
     * @return the ExpansionManifest
     */
    public ExpansionManifest toManifest(PlistParser.DictionaryContext dc) {
        log.debug("toManifest({})", dc);
        ExpansionManifest em = new ExpansionManifest();
        
        for (PlistParser.KeyvaluepairContext kc: dc.keyvaluepair()) {
            evaluateOxpKeys(kc, em);
        }
        return em;
    }

    /**
     * Adds a warning.
     * 
     * @param warning the warning
     */
    public void addWarning(String warning) {
        warnings.add(warning);
    }

    /** 
     * Returns global list of warnings and all the others.
     * 
     * @return the list
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
        
    /**
     * Returns the property with given key's value.
     * @param key the key
     * @return the value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Sets a property.
     * 
     * @param key the key
     * @param value the value
     */
    public void setProperty(String key, String value) {
        try {
            if (value == null) {
                properties.remove(key);
            } else {
                properties.setProperty(key, value);
            }
        } catch (NullPointerException e) {
            throw new NullPointerException(String.format("Could not set property %s to %s", key, value));
        }
    }

    /**
     * Returns all properties.
     * 
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }
    
}
