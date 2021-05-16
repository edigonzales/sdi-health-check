package ch.so.agi.healthcheck.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    
    @OneToMany(
            mappedBy = "probeVars",
            cascade = CascadeType.ALL,
            orphanRemoval = true
        )
    private List<CheckVars> checksVars = new ArrayList<>();

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
    
    public void setChecks(List<CheckVars> checksVars) {
        this.checksVars = checksVars;
        for (CheckVars checkVars : this.checksVars) {
            checkVars.setProbeVars(this);
        }
    }
    
    public void addCheck(CheckVars checkVars) {
        this.checksVars.add(checkVars);
        checkVars.setProbeVars(this);
    }
    
    public List<CheckVars> getCheckVars() {
        return this.checksVars;
    }
    
//    public void removeCheck(CheckVars checkVars) {
//        this.checksVars.remove(checkVars);
//        checkVars.setProbeVars(null);
//    }
}
