package ch.so.agi.healthcheck;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ch.so.agi.healthcheck.model.ResourceDTO;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class Config {
    private List<ResourceDTO> resources = new ArrayList<ResourceDTO>();

    public List<ResourceDTO> getResources() {
        return resources;
    }

    public void setResources(List<ResourceDTO> resources) {
        this.resources = resources;
    }
}
