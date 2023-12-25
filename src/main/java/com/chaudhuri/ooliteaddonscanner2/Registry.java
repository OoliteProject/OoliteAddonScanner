/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Warnable;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    public void addExpansions(NSArray lc) {
        log.debug("addExpansions({})", lc);
        
        if (lc == null) {
            throw new IllegalArgumentException("lc must not be null");
        }
        
        // we expect a list of dictionaries, nothing else
        
        for (NSObject vc: lc.getArray()) {
            NSDictionary expansion = (NSDictionary)vc;
            if (log.isTraceEnabled()) {
                log.trace("{} {}", expansion.getClass(), expansion);
            }
            addExpansion(expansion);
        }
    }

    /** Adds a sinble expansion. The Expansion is parsed from the
     * plist from the Expansion Manager.
     * 
     * @param dc 
     */
    public void addExpansion(NSDictionary dc) {
        log.debug("addExpansion({})", dc);
        
        Expansion oxp = new Expansion();
        
        for (Entry<String, NSObject> kc: dc.entrySet()) {
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
    
    private static void evaluateOxpKeys(Map.Entry<String, NSObject> kc, Expansion oxp) {

        String key = kc.getKey();
            
        if (EXPANSION_IDENTIFIER.equals(key)) {
            oxp.setIdentifier(kc.getValue().toString());
        } else if (EXPANSION_REQUIRED_OOLITE_VERSION.equals(key)) {
            oxp.setRequiredOoliteVersion(kc.getValue().toString());
        } else if (EXPANSION_TITLE.equals(key)) {
            oxp.setTitle(kc.getValue().toString());
        } else if (EXPANSION_VERSION.equals(key)) {
            oxp.setVersion(kc.getValue().toString());
        } else if (EXPANSION_CATEGORY.equals(key)) {
            oxp.setCategory(kc.getValue().toString());
        } else if (EXPANSION_DESCRIPTION.equals(key)) {
            oxp.setDescription(kc.getValue().toString());
        } else if ("download_url".equals(key)) {
            oxp.setDownloadUrl(kc.getValue().toString());
        } else if (EXPANSION_AUTHOR.equals(key)) {
            oxp.setAuthor(kc.getValue().toString());
        } else if ("file_size".equals(key)) {
            oxp.setFileSize(kc.getValue().toString());
        } else if (EXPANSION_INFORMATION_URL.equals(key)) {
            oxp.setInformationUrl(kc.getValue().toString());
        } else {
            evaluateOxpKeys2(kc, oxp);
        }
    }
    
    /**
     * 
     * @param vc
     * @param warnable
     * @return 
     * @see https://wiki.alioth.net/index.php/Manifest.plist#Dependency_management_keys
     */
    private static List<Expansion.Dependency> parseDependencies(NSArray vc, Warnable warnable) {
        List<Expansion.Dependency> result = new ArrayList<>();
        
        for (NSObject vc2: vc.getArray()) {
            NSDictionary dict = (NSDictionary)vc2;

            Expansion.Dependency dependency = new Expansion.Dependency();

            for (Map.Entry<String, NSObject> kvc: dict.entrySet()) {
                String key = kvc.getKey();
                String value = kvc.getValue().toString();
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
        
        return result;
    }
    
    /**
     * Reads an NSArray of NSString and returns a List of Strings.
     * 
     * @param list
     * @return 
     */
    private static List<String> parseStringList(NSArray list) {
        ArrayList<String> result = new ArrayList<>();
        for (NSObject o: list.getArray()) {
            result.add(o.toString());
        }
        return result;
    }

    private static void evaluateOxpKeys2(Map.Entry<String, NSObject> kc, Expansion oxp) {
        String key = kc.getKey();
        if (EXPANSION_LICENSE.equals(key)) {
            oxp.setLicense(kc.getValue().toString());
        } else if ("upload_date".equals(key)) {
            oxp.setUploadDate(kc.getValue().toString());
        } else if (EXPANSION_TAGS.equals(key)) {
            oxp.setTags(parseStringList((NSArray)kc.getValue())); //TODO
        } else if (EXPANSION_REQUIRES_OXPS.equals(key)) {
            oxp.setRequiresOxps(parseDependencies((NSArray)kc.getValue(), oxp));
        } else if (EXPANSION_OPTIONAL_OXPS.equals(key)) {
            oxp.setOptionalOxps(parseDependencies((NSArray)kc.getValue(), oxp));
        } else if (EXPANSION_CONFLICT_OXPS.equals(key)) {
            oxp.setConflictOxps(parseDependencies((NSArray)kc.getValue(), oxp));
        } else if (EXPANSION_MAXIMUM_OOLITE_VERSION.equals(key)) {
            oxp.setMaximumOoliteVersion(kc.getValue().toString());
        } else {
            log.warn("Could not process key '{}'", key);
        }
    }
    
    private static void evaluateOxpKeys(Map.Entry<String, NSObject> kc, ExpansionManifest em) {
        String key = kc.getKey();
        
        if (EXPANSION_IDENTIFIER.equals(key)) {
            em.setIdentifier(kc.getValue().toString());
        } else if (EXPANSION_AUTHOR.equals(key)) {
            em.setAuthor(kc.getValue().toString());
        } else if (EXPANSION_CONFLICT_OXPS.equals(key)) {
            em.setConflictOxps(parseDependencies((NSArray)kc.getValue(), em));
        } else if (EXPANSION_DESCRIPTION.equals(key)) {
            em.setDescription(kc.getValue().toString());
        } else if ("download_url".equals(key)) {
            em.setDownloadUrl(kc.getValue().toString());
        } else if (EXPANSION_CATEGORY.equals(key)) {
            em.setCategory(kc.getValue().toString());
        } else if ("file_size".equals(key)) {
            em.setFileSize(kc.getValue().toString());
        } else {
            evaluateOxpKeys2(kc, em);
        }
    }
    
    private static void evaluateOxpKeys2(Map.Entry<String, NSObject> kc, ExpansionManifest em) {
        String key = kc.getKey();
        
        if (EXPANSION_INFORMATION_URL.equals(key)) {
            em.setInformationUrl(kc.getValue().toString());
        } else if (EXPANSION_LICENSE.equals(key)) {
            em.setLicense(kc.getValue().toString());
        } else if (EXPANSION_MAXIMUM_OOLITE_VERSION.equals(key)) {
            em.setMaximumOoliteVersion(kc.getValue().toString());
        } else if (EXPANSION_OPTIONAL_OXPS.equals(key)) {
            em.setOptionalOxps(parseDependencies((NSArray)kc.getValue(), em));
        } else if (EXPANSION_REQUIRED_OOLITE_VERSION.equals(key)) {
            em.setRequiredOoliteVersion(kc.getValue().toString());
        } else if (EXPANSION_REQUIRES_OXPS.equals(key)) {
            em.setRequiresOxps(parseDependencies((NSArray)kc.getValue(), em));
        } else if (EXPANSION_TAGS.equals(key)) {
            em.setTags(parseStringList((NSArray)kc.getValue()));
        } else if (EXPANSION_TITLE.equals(key)) {
            em.setTitle(kc.getValue().toString());
        } else if (EXPANSION_VERSION.equals(key)) {
            em.setVersion(kc.getValue().toString());
        } else {
            log.trace("Unknown key {}->{}", key, kc.getValue().toString());
            em.addWarning(String.format("Unknown key '%s'", key));
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
    public void addEquipmentList(Expansion expansion, NSArray lc) throws RegistryException {
        log.debug("addEquipmentList({}, {})", expansion, lc);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (lc==null) {
            throw new IllegalArgumentException("ListContext must not be null");
        }
        
        // for each equipment
        for (NSObject vc: lc.getArray()) {
            
            // the equipment is a list by itself
            log.trace("ValueContext: {}", vc.toString());
            if (vc instanceof NSArray) {
                addEquipment(expansion, (NSArray)vc);
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
     * @see https://wiki.alioth.net/index.php/Equipment.plist#Equipment_Structure
     */
    public void addEquipment(Expansion expansion, NSArray lc) throws RegistryException {
        log.debug("addEquipment({}, {})", expansion, lc);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (lc == null) {
            throw new IllegalArgumentException("lc must not be null");
        }
        
        Equipment eq = new Equipment();
        eq.setExpansion(expansion);

        eq.setTechlevel  (lc.getArray()[0].toString());
        eq.setCost       (lc.getArray()[1].toString());
        eq.setName       (lc.getArray()[2].toString());
        eq.setIdentifier (lc.getArray()[3].toString());
        eq.setDescription(lc.getArray()[4].toString());
        
        try {
            NSObject v = lc.getArray()[5];
            if (v != null) {
                NSDictionary d = (NSDictionary)v;
                for (Map.Entry<String, NSObject> kc: d.entrySet()) {
                    eq.putFeature(kc.getKey(), kc.getValue().toString());
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
    public void addShipList(Expansion expansion, NSDictionary dc) {
        log.debug("addShipList({}, {})", expansion, dc);
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        if (dc == null) {
            throw new IllegalArgumentException("dc must not be null");
        }
        
        for (Map.Entry<String, NSObject> kc: dc.entrySet()) {
            String key = kc.getKey(); // ship identifier
            NSDictionary ship = (NSDictionary)kc.getValue();
            
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
    public void addShip(Expansion expansion, String identifier, NSDictionary dc) {
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
        
        for (Map.Entry<String, NSObject> kc: dc.entrySet()) {
            ship.addFeature(kc.getKey(), kc.getValue().toString());
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
            
            if (i==null && i1 == null) {
                return 0;
            }
            if (i==null) {
                return -1;
            }
            if (i1==null) {
                return 1;
            }
            
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
     * @return the ExpansionManifest
     */
    public ExpansionManifest toManifest(NSDictionary dc) {
        log.debug("toManifest({})", dc);
        ExpansionManifest em = new ExpansionManifest();
        
        for (Map.Entry<String, NSObject> kc: dc.entrySet()) {
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
