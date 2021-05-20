package ch.so.agi.healthcheck.probe;

import ch.so.agi.healthcheck.Result;

public class ProbeResult extends Result {
    private Probe probe;
    
    public ProbeResult(Probe probe) {
        this.probe = probe;
        this.className = probe.getClass().getCanonicalName();
    }
}
