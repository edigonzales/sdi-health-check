package ch.so.agi.healthcheck.probe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.Resource;

@Service
public class OwsGetCaps extends Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public OwsGetCaps() {
        super();
    }
    
    public OwsGetCaps(Resource resource, ProbeVars probeVars) {
        super(resource, probeVars);
    }

    @Override
    public void performRequest() {
        log.info("performRequest");
    }
}
