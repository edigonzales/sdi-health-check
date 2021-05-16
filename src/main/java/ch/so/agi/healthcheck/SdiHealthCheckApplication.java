package ch.so.agi.healthcheck;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobScheduler;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

import ch.so.agi.healthcheck.model.CheckVars;
import ch.so.agi.healthcheck.model.ProbeVars;
import ch.so.agi.healthcheck.model.Resource;
import ch.so.agi.healthcheck.model.ResourceDTO;
import ch.so.agi.healthcheck.model.ResourceType;
import ch.so.agi.healthcheck.probe.WmsGetCaps;
import ch.so.agi.healthcheck.probe.Runner;
import ch.so.agi.healthcheck.repository.ResourceRepository;

@SpringBootApplication
public class SdiHealthCheckApplication {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Config config;

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
            ResourceDTO resourceDTO = config.getResources().get(0);

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);

            Resource resource = modelMapper.map(resourceDTO, Resource.class);

            for (ProbeVars probeVars : resource.getProbesVars()) {
                String jobId = UUID.randomUUID().toString();
                probeVars.setJobrunrId(jobId);
            }

            resourceRepository.save(resource);

            CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);
            CronParser parser = new CronParser(cronDefinition);

            for (ProbeVars probeVars : resource.getProbesVars()) {
                jobScheduler.<Runner>enqueue(x -> x.run(resource.getId()));
//                try {
//                    Cron quartzCron = parser.parse(resource.getRunFrequency());
//                    quartzCron.validate();
//                    jobScheduler.<Runner>scheduleRecurrently(probeVars.getJobrunrId(), resource.getRunFrequency(), x -> x.run(resource.getId()));
//                } catch (IllegalArgumentException e) {
//                    // TODO: do something
//                    throw new IllegalArgumentException(e);
//                }
            }
        };
    }
}
