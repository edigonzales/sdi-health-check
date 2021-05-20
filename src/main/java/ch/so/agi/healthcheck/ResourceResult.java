package ch.so.agi.healthcheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.so.agi.healthcheck.model.ResourceDTO;
import ch.so.agi.healthcheck.probe.ProbeResult;

public class ResourceResult extends Result {

    private ResourceDTO resource;
    
    public ResourceResult(ResourceDTO resource) {
        this.resource = resource;
    }
    
    @Override
    public Map<String, Object> getRawReport() {
        String pattern = "HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        reportMap = new HashMap<>();
        reportMap.put("name", "todo: resource name");
        reportMap.put("description", "todo: resource description");
        reportMap.put("type", resource.getType());
        reportMap.put("title", resource.getTitle());
        reportMap.put("url", resource.getUrl());
        reportMap.put("success", this.success);
        reportMap.put("message", this.message);
        reportMap.put("start_time", simpleDateFormat.format(Date.from(startTime)));
        reportMap.put("end_time", simpleDateFormat.format(Date.from(endTime)));
        reportMap.put("response_time", this.responseTimeSecs);
        
        List<Map<String,Object>> reports = new ArrayList<>();
        for (Result result : this.results) {
            ProbeResult probeResult = ((ProbeResult) result);
            reports.add(probeResult.getRawReport());
        }
        reportMap.put("probes", reports);

        return reportMap;
    }
}
