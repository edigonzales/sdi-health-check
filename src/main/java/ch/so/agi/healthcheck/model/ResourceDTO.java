package ch.so.agi.healthcheck.model;

import java.util.ArrayList;
import java.util.List;


public class ResourceDTO {
    private Long id;
    
    private ResourceType type;
    
    private boolean active = true;
    
    private String title;
    
    private String url;
    
    private int runFrequency;
    
    private List<ProbeVarsDTO> probesVars = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRunFrequency() {
        return runFrequency;
    }

    public void setRunFrequency(int runFrequency) {
        this.runFrequency = runFrequency;
    }

    public List<ProbeVarsDTO> getProbesVars() {
        return probesVars;
    }

    public void setProbesVars(List<ProbeVarsDTO> probesVars) {
        this.probesVars = probesVars;
    }
}
