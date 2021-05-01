package ch.so.agi.healthcheck;

import javax.sql.DataSource;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.Resource;
import ch.so.agi.healthcheck.model.ResourceType;
import ch.so.agi.healthcheck.probe.OwsGetCaps;
import ch.so.agi.healthcheck.probe.Probe;
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
	public CommandLineRunner init(ResourceRepository resourceRepository) {
	    return args -> {
	        Resource resource = new Resource();
	        resource.setType(ResourceType.OGC_WMS);
	        resource.setTitle("SOGIS WMS (Prod)");
	        resource.setUrl("https://geo.so.ch/wms");
	        resource.setActive(true);
	        resource.setRunFrequency(60);
	        
	       

	        JobId jobId = jobScheduler.enqueue(() -> System.out.println("Hallo Welt."));
	        jobScheduler.<OwsGetCaps>scheduleRecurrently(x -> x.performRequest(), "* * * * *");
	      
	        ProbeVars probeVars = new ProbeVars("ch.so.agi.healthcheck.probe.WmsGetCaps", "{\"service\": \"WMS\", \"version\": \"1.3.0\"}");
            probeVars.setJobrunrId(jobId.asUUID().toString());
            resource.addProbe(probeVars);

	        
	        
	        resourceRepository.save(resource);
	    };
	}
}
