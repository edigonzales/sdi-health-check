package ch.so.agi.healthcheck.check;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.Result;

public class CheckResult extends Result {
    private Check check;
    
    public CheckResult(Check check) {
        this.check = check;
    }
    
    // TODO: report pojo? doch sinnvoll?
    public String getReport() {
        // TODO name!
        reportMap.put("class", this.check.getClass().getCanonicalName());
        reportMap.put("success", this.success);
        reportMap.put("message", this.message);
        reportMap.put("response_time", String.valueOf(this.responseTimeSecs));
        try {
            return new ObjectMapper().writeValueAsString(reportMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    public Map<String,Object> getRawReport() {
        this.getReport();
        return reportMap;
    }
}
