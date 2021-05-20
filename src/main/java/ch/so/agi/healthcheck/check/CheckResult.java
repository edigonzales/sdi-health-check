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
    
    @Override
    public Map<String,Object> getRawReport() {
        reportMap = new HashMap<>();
        reportMap.put("class", this.check.getClass().getCanonicalName());
        reportMap.put("name", "todo: check name");
        reportMap.put("description", "todo: check description");
        reportMap.put("success", this.success);
        reportMap.put("message", this.message);
        reportMap.put("response_time", this.responseTimeSecs);

        return reportMap;
    }
}
