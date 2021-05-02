package ch.so.agi.healthcheck.probe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbeFactory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Probe getProbe(String probeType) {
        if (probeType == null) {
            return null;
        } else if (probeType.equalsIgnoreCase("ch.so.agi.healthcheck.probe.WmsGetCaps")) {
            return new ch.so.agi.healthcheck.probe.WmsGetCaps();
        }
        return null;
    }
    
}
