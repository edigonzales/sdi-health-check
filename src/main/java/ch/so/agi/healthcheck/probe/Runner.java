package ch.so.agi.healthcheck.probe;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.ProbeVarsDTO;
import ch.so.agi.healthcheck.model.Resource;
import ch.so.agi.healthcheck.model.ResourceDTO;
import ch.so.agi.healthcheck.repository.ResourceRepository;

@Service
public class Runner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ResourceRepository resourceRepository;

    // TODO: use jdbc templates instead (?). read only.
    // oder DTO projection?
    // oder eager (in unserem Fall wohl kein Drama).
    @Transactional
    public void run(Long id) {
        log.info("id: " + id);
        
        Resource resource = resourceRepository.findById(id).orElseThrow();
        
        // Brauche ich wahrscheinlich nicht mehr. Die DTO werden von Spring Data befühlt.
        // Ich will hier nichts mehr mit JPA zu tun haben (?!)
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE);

        ResourceDTO resourceDTO = modelMapper.map(resource, ResourceDTO.class);
        
        List<ProbeVarsDTO> probesVars = resourceDTO.getProbesVars();
        
        for (ProbeVarsDTO probeVars : probesVars) {
            log.info(probeVars.getJobrunrId());
            log.info(probeVars.getParameters());    
            log.info(probeVars.getProbeClass());
        
            ProbeFactory probeFactory = new ProbeFactory();
            Probe probe = probeFactory.getProbe(probeVars.getProbeClass());
            log.info(probe.getClass().toString());
            
//            probe.setProbeVars(probeVars);
//            probe.setResourceUrl(resource.getUrl());
            
            probe.run(resource.getUrl(), probeVars);
            
        
        }

        
        
    }
}
