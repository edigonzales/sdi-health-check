package ch.so.agi.healthcheck.probe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.Result;
import ch.so.agi.healthcheck.check.CheckResult;

public class ProbeResult extends Result {
    private Probe probe;
    
    public ProbeResult(Probe probe) {
        this.probe = probe;
    }

    public String getReport() {
        reportMap.put("class", this.probe.getClass().getCanonicalName());
        reportMap.put("success", this.success);
        reportMap.put("message", this.message);
        reportMap.put("response_time", String.valueOf(this.responseTimeSecs));
        
        List<Map> reports = new ArrayList<>();
        for (Result result : this.results) {
            reports.add(((CheckResult) result).getRawReport());
        }
        reportMap.put("reports", reports);
        
        try {
            return new ObjectMapper().writeValueAsString(reportMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRawReport() {
        this.getReport();
        return reportMap;
    }

}
