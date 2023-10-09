/*
 */

package com.chaudhuri.cataloggenerator;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion.Dependency;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Genson converter to serialize ExpansionManifest.
 * @author hiran
 */
public class ExpansionManifestConverter implements Converter<ExpansionManifest> {
    private static final Logger log = LogManager.getLogger();

    @Override
    public void serialize(ExpansionManifest em, ObjectWriter writer, Context cntxt) throws Exception {
        log.debug("serialize({}, {}, {})", em, writer, cntxt);
        writer.beginObject();
        writer.writeString("identifier", em.getIdentifier());
        writer.writeString("version", em.getVersion());
        writer.writeString("required_oolite_version", em.getRequiredOoliteVersion());
        writer.writeString("maximum_oolite_version", em.getMaximumOoliteVersion());
        writer.writeString("title", em.getTitle());
        writer.writeString("description", em.getDescription());
        writer.writeString("category", em.getCategory());
        writer.writeString("author", em.getAuthor());
        writer.writeString("license", em.getLicense());
        writer.writeString("download_url", em.getDownloadUrl());
        writer.writeString("information_url", em.getInformationUrl());
        writer.writeNumber("file_size", Long.parseLong(em.getFileSize()));
        writer.writeNumber("upload_date", em.getUploadDate());
        writer.writeName("tags");
        writer.beginArray();
        for (String t: em.getTags()) {
            writer.writeValue(t);
        }
        writer.endArray();
        writer.writeName("conflict_oxps");
        writer.beginArray();
        for (Dependency dep: em.getConflictOxps()) {
            writer.beginObject();
            writer.writeString("identifier", dep.getIdentifier());
            writer.writeString("version", dep.getVersion());
            writer.writeString("maximum_version", dep.getMaxVersion());
            writer.writeString("description", dep.getDescription());
            writer.endObject();
        }
        writer.endArray();
        writer.writeName("optional_oxps");
        writer.beginArray();
        for (Dependency dep: em.getOptionalOxps()) {
            writer.beginObject();
            writer.writeString("identifier", dep.getIdentifier());
            writer.writeString("version", dep.getVersion());
            writer.writeString("maximum_version", dep.getMaxVersion());
            writer.writeString("description", dep.getDescription());
            writer.endObject();
        }
        writer.endArray();
        writer.writeName("requires_oxps");
        writer.beginArray();
        for (Dependency dep: em.getRequiresOxps()) {
            writer.beginObject();
            writer.writeString("identifier", dep.getIdentifier());
            writer.writeString("version", dep.getVersion());
            writer.writeString("maximum_version", dep.getMaxVersion());
            writer.writeString("description", dep.getDescription());
            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
    }

    @Override
    public ExpansionManifest deserialize(ObjectReader reader, Context cntxt) throws Exception {
        log.debug("deserialize(...)");
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
