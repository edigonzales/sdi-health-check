package ch.so.agi.healthcheck;

import java.util.UUID;

import javax.sql.DataSource;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.Resource;
import ch.so.agi.healthcheck.model.ResourceType;
import ch.so.agi.healthcheck.probe.WmsGetCaps;
import ch.so.agi.healthcheck.probe.Probe;
import ch.so.agi.healthcheck.probe.Runner;
import ch.so.agi.healthcheck.repository.ResourceRepository;

@SpringBootApplication
public class SdiHealthCheckApplication {

    @Autowired
    DataSource dataSource;

    @Autowired 
    JobScheduler jobScheduler;
        
	public static void main(String[] args) {
		SpringApplication.run(SdiHealthCheckApplication.class, args);
	}
	
	@Bean
    @Transactional
	public CommandLineRunner init(ResourceRepository resourceRepository) {
	    return args -> {
	        Resource resource = new Resource();
	        resource.setType(ResourceType.OGC_WMS);
	        resource.setTitle("SOGIS WMS (Prod)");
	        resource.setUrl("http://map.geo.gl.ch/ows/mainmap");
//            resource.setUrl("https://geo.so.ch/wms");
//            resource.setUrl("https://wms.geo.admin.ch/");
	        resource.setActive(true);
	        resource.setRunFrequency(60);
	        
	       

//	        JobId jobId = jobScheduler.enqueue(() -> System.out.println("Hallo Welt."));
	        
            String jobId = UUID.randomUUID().toString();

	        ProbeVars probeVars = new ProbeVars("ch.so.agi.healthcheck.probe.WmsGetCaps", "{\"service\": \"WMS\", \"version\": \"1.3.0\"}");	      
            probeVars.setJobrunrId(jobId);

	        resource.addProbe(probeVars);
            resourceRepository.save(resource);
                        

            //jobScheduler.<WmsGetCaps>scheduleRecurrently(jobId, x -> x.run(resource.getId()), "* * * * *");
            jobScheduler.<Runner>enqueue(x -> x.run(resource.getId()));

            
	        
	    };
	}
}
