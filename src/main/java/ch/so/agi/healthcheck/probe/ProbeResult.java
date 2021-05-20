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

    @Override
    public Map<String, Object> getRawReport() {
        reportMap = new HashMap<>();
        reportMap.put("class", this.probe.getClass().getCanonicalName());
        reportMap.put("name", "todo");
        reportMap.put("description", "todo");
        reportMap.put("success", this.success);
        reportMap.put("message", this.message);
        reportMap.put("response_time", this.responseTimeSecs);
        
        List<Map<String,Object>> reports = new ArrayList<>();
        for (Result result : this.results) {
            CheckResult checkResult = ((CheckResult) result);
            reports.add(checkResult.getRawReport());
        }
        reportMap.put("checks", reports);

        return reportMap;
    }

}
