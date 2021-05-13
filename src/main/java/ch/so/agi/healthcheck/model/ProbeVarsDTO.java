package ch.so.agi.healthcheck.model;

import java.util.ArrayList;
import java.util.List;

public class ProbeVarsDTO {
    private Long id;

    private String jobrunrId;
    
    private String probeClass;
    
    private String parameters;

    private List<CheckVarsDTO> checksVars = new ArrayList<>();

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

    public List<CheckVarsDTO> getChecksVars() {
        return checksVars;
    }

    public void setChecksVars(List<CheckVarsDTO> checksVars) {
        this.checksVars = checksVars;
    }
}
