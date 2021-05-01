package ch.so.agi.healthcheck.probe;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.Resource;

public abstract class Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";
    
    private Map<String, String> requestHeaders = new HashMap<String, String>();

    private Map<String, String> standardRequestHeaders = new HashMap<String, String>() {{
       put("User-Agent", "SdiHealthCheck");
       put("Accept-Encoding", "deflate, gzip;q=1.0, *;q=0.5");
    }};
    
    private String requestTemplate;
 
    private String paramDefs;
    
    private Resource resource;
    
    private ProbeVars probeVars;
    
    public Probe() {}
    
    public Probe(Resource resource, ProbeVars probeVars) {
        this.resource = resource;
        this.probeVars = probeVars;
    }
    
    public void performRequest() {
        log.info("performRequets");
    }

    
}
