package ch.so.agi.healthcheck.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;


@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private ResourceType type;
    
    private boolean active = true;
    
    @NotNull
    private String title;
    
    @NotNull
    private String url;
    
    @NotNull
    private String runFrequency;
    
    @OneToMany(
            mappedBy = "resource",
            cascade = CascadeType.ALL,
            orphanRemoval = true
        )
    private List<ProbeVars> probesVars = new ArrayList<>();

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

    public String getRunFrequency() {
        return runFrequency;
    }

    public void setRunFrequency(String runFrequency) {
        this.runFrequency = runFrequency;
    }

    public List<ProbeVars> getProbesVars() {
        return probesVars;
    }

    public void setProbes(List<ProbeVars> probesVars) {
        this.probesVars = probesVars;
        for (ProbeVars probeVars : this.probesVars) {
            probeVars.setResource(this);
        }
    }

    public void addProbe(ProbeVars probeVars) {
        probesVars.add(probeVars);
        probeVars.setResource(this);
    }
    
    public void removeProbe(ProbeVars probeVars) {
        probesVars.remove(probeVars);
        probeVars.setResource(null);
    }
    
}
