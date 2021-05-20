package ch.so.agi.healthcheck.probe;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.so.agi.healthcheck.ResourceResult;
import ch.so.agi.healthcheck.check.CheckResult;
import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.ProbeVarsDTO;
import ch.so.agi.healthcheck.model.Resource;
import ch.so.agi.healthcheck.model.ResourceDTO;
import ch.so.agi.healthcheck.model.Run;
import ch.so.agi.healthcheck.repository.ResourceRepository;
import ch.so.agi.healthcheck.repository.RunRepository;

@Service
public class Runner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ResourceRepository resourceRepository;
    
    @Autowired
    RunRepository runRepository;


    // Würde wahrscheinlich so eh nicht mehr gehen, wenn die Klasse in einem anderen
    // Container läuft... Oder dann erst recht wieder (eigentlich)?
    // TODO: use jdbc templates instead (?). read only.
    // oder DTO projection?
    // oder eager (in unserem Fall wohl kein Drama).
    // oder doch DTO übergeben, das kann ja jetzt einfach serialisert werden und ist nicht extrem gross.
    @Transactional
    public void run(Long id) {
        log.info("id: " + id);
        
        Resource resource = resourceRepository.findById(id).orElseThrow();
        
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE);

        ResourceDTO resourceDTO = modelMapper.map(resource, ResourceDTO.class);
        List<ProbeVarsDTO> probesVars = resourceDTO.getProbesVars();
        
        ResourceResult resourceResult = new ResourceResult(resourceDTO);
        resourceResult.start();
        
        for (ProbeVarsDTO probeVars : probesVars) {
            ProbeFactory probeFactory = new ProbeFactory();
            Probe probe = probeFactory.getProbe(probeVars.getProbeClass());                    
            try {
                probe.run(resourceDTO, probeVars);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                probe.getProbeResult().setMessage(e.getMessage());
            }
            resourceResult.addResult(probe.getProbeResult());
        }
        
        resourceResult.stop();
        
        Run runObj = new Run();
        runObj.setCheckedDatetime(new Date());
        runObj.setSuccess(resourceResult.isSuccess());
        runObj.setMessage(resourceResult.getMessage());
        runObj.setReport(resourceResult.getReport());
        runObj.setResponseTime(resourceResult.getResponseTimeSecs());
        runObj.setResource(resource);
        runRepository.save(runObj);
    }
}
