package ch.so.agi.healthcheck.probe;

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
        
        // Brauche ich wahrscheinlich nicht mehr. Die DTO werden von Spring Data befüllt.
        // Ich will hier nichts mehr mit JPA zu tun haben (?!)
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE);

        ResourceDTO resourceDTO = modelMapper.map(resource, ResourceDTO.class);
        
        List<ProbeVarsDTO> probesVars = resourceDTO.getProbesVars();
        
        // TODO:
        // responseTime vs. elapsedTime.
        // a) naming?
        // b) summe aller probes elapsedTime ergibt resonseTime von run.
        // Eigentlich passt mir responseTime nicht so, erinnert doch stark an request/response.
        // Aber vielleicht doch ok. Oder nur dort, wo es wirklich ein request/response im Check gibt? Z.B. getMap().
        // Was ist dann mit getCapabilities?
        // Probe _und_ Check haben responseTime.
        // Konzentrierter anschauen morgen.
        
        for (ProbeVarsDTO probeVars : probesVars) {
            log.info(probeVars.getJobrunrId());
            log.info(probeVars.getParameters());    
            log.info(probeVars.getProbeClass());
        
            ProbeFactory probeFactory = new ProbeFactory();
            Probe probe = probeFactory.getProbe(probeVars.getProbeClass());
            log.info(probe.getClass().toString());
                    
            Instant startProbe = Instant.now();
            ProbeResult2 result = probe.run(resourceDTO, probeVars);
            Instant finishProbe = Instant.now();
            
            long elapsedTime = Duration.between(startProbe, finishProbe).toMillis();
            result.setElapsedTime(elapsedTime);
            
            Run runObj = new Run();
            runObj.setCheckedDatetime(new Date());
            
            if(result.isSuccess()) {
                runObj.setMessage("OK");
                runObj.setSuccess(true);
            } else {
                // TODO stream api?
                List<String> checkResultMessages = new ArrayList<String>();
                for (CheckResult checkResult : result.getCheckResults()) {
                    if(!checkResult.isSuccess()) {
                        checkResultMessages.add(checkResult.getMessage());
                    }
                }
                runObj.setReport(null); // TODO: mit jackson serialisieren
                runObj.setMessage(String.join(", ", checkResultMessages));
                runObj.setSuccess(false);
            }
            
            runObj.setResource(resource);
            runRepository.save(runObj);
        }
    }
}
