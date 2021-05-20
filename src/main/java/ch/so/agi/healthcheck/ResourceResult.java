package ch.so.agi.healthcheck;

import ch.so.agi.healthcheck.model.ResourceDTO;
import ch.so.agi.healthcheck.model.ResourceType;

public class ResourceResult extends Result {

    private ResourceDTO resource;
    
    private ResourceType type;
    
    private String title;
    
    private String url;
    
   
    public ResourceResult(ResourceDTO resource) {
        this.resource = resource;
        this.type = resource.getType();
        this.title = resource.getTitle();
        this.url = resource.getUrl();
    }
    
    public ResourceType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }    
}
