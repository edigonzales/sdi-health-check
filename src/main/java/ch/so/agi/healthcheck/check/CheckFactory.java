package ch.so.agi.healthcheck.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckFactory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static Check getCheck(String probeCheck) {
        if (probeCheck == null) {
            return null;
        } else if (probeCheck.equalsIgnoreCase("ch.so.agi.healthcheck.check.HttpStatusNoError")) {
            return new ch.so.agi.healthcheck.check.HttpStatusNoError();
        } else if (probeCheck.equalsIgnoreCase("ch.so.agi.healthcheck.check.HasUniqueWmsLayerIdentifiers")) {
            return new ch.so.agi.healthcheck.check.HasUniqueWmsLayerIdentifiers();
        } else if (probeCheck.equalsIgnoreCase("ch.so.agi.healthcheck.check.HttpHasHeaderValue")) {
            return new ch.so.agi.healthcheck.check.HttpHasHeaderValue();
        } else if (probeCheck.equalsIgnoreCase("ch.so.agi.healthcheck.check.HttpHasImageContentType")) {
            return new ch.so.agi.healthcheck.check.HttpHasImageContentType();
        } else if (probeCheck.equalsIgnoreCase("ch.so.agi.healthcheck.check.XmlParse")) {
            return new ch.so.agi.healthcheck.check.XmlParse();
        } else if (probeCheck.equalsIgnoreCase("ch.so.agi.healthcheck.check.ContainsStrings")) {
            return new ch.so.agi.healthcheck.check.ContainsStrings();
        } else if (probeCheck.equalsIgnoreCase("ch.so.agi.healthcheck.check.NotContainsStrings")) {
            return new ch.so.agi.healthcheck.check.NotContainsStrings();
        }
        
        return null;
    }
    
}
