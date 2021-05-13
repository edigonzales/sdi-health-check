package ch.so.agi.healthcheck.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckFactory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static Check getProbe(String probeType) {
        if (probeType == null) {
            return null;
        } else if (probeType.equalsIgnoreCase("ch.so.agi.healthcheck.check.HttpStatusNoError")) {
            return new ch.so.agi.healthcheck.check.HttpStatusNoError();
        }
        return null;
    }
    
}
