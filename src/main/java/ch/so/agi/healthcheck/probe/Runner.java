package ch.so.agi.healthcheck.probe;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.Resource;
import ch.so.agi.healthcheck.repository.ResourceRepository;

@Service
public class Runner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ResourceRepository resourceRepository;

    // TODO: use jdbc templates instead (?). read only.
    
    @Transactional
    public void run(Long id) {
        log.info("id: " + id);
        
        Resource resource = resourceRepository.findById(id).orElseThrow();
        
        List<ProbeVars> probesVars = resource.getProbesVars();
        
        for (ProbeVars probeVars : probesVars) {
            log.info(probeVars.getJobrunrId());
            log.info(probeVars.getParameters());    
            log.info(probeVars.getProbeClass());
        
            ProbeFactory probeFactory = new ProbeFactory();
            IProbe probe = probeFactory.getProbe(probeVars.getProbeClass());
            log.info(probe.getClass().toString());
            
//            probe.setProbeVars(probeVars);
//            probe.setResourceUrl(resource.getUrl());
            
            probe.run(resource.getUrl(), probeVars.getParameters());
            
        
        }

        
        
    }
}
