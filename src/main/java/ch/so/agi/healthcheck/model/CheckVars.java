package ch.so.agi.healthcheck.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CheckVars {
    public CheckVars() {}
    
    public CheckVars(String checkClass, String parameters) {
        this.checkClass = checkClass;
        this.parameters = parameters;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private String checkClass;
    
    private String parameters;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ProbeVars probeVars;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheckClass() {
        return checkClass;
    }

    public void setCheckClass(String checkClass) {
        this.checkClass = checkClass;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public ProbeVars getProbeVars() {
        return probeVars;
    }

    public void setProbeVars(ProbeVars probeVars) {
        this.probeVars = probeVars;
    }
}
