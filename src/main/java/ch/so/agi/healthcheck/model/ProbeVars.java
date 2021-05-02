package ch.so.agi.healthcheck.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProbeVars {
    public ProbeVars() {}
    
    public ProbeVars(String probeClass, String parameters) {
        this.probeClass = probeClass;
        this.parameters = parameters;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String jobrunrId;
    
    private String probeClass;
    
    private String parameters;
   
    @ManyToOne(fetch = FetchType.LAZY)
    private Resource resource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobrunrId() {
        return jobrunrId;
    }

    public void setJobrunrId(String jobrunrId) {
        this.jobrunrId = jobrunrId;
    }

    public String getProbeClass() {
        return probeClass;
    }

    public void setProbeClass(String probeClass) {
        this.probeClass = probeClass;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
