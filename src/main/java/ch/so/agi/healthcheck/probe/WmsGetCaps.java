package ch.so.agi.healthcheck.probe;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.so.agi.healthcheck.check.CheckFactory;
import ch.so.agi.healthcheck.check.Check;
import ch.so.agi.healthcheck.model.CheckVars;
import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.model.ProbeVarsDTO;

@Service
public class WmsGetCaps implements Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    @ParamDefinition(name = "service", description = "The WMS service within resource endpoint")
    @ParamDefinition(name = "version", description = "The WMS service version within resource endpoint")
    private String requestTemplate = "?SERVICE=${service}&VERSION=${version}&REQUEST=GetCapabilities";
    
    // TODO: mit getXXXXXX im Interface könnte man es wohl schon noch so machen, dass man run nicht
    // zu implementieren braucht im Regelfall.
    @Override
    public void run(String url, ProbeVarsDTO probeVars) {
        ProbeResult result = new ProbeResult();
        
        this.beforeRequest();
        try {
            HttpResponse<InputStream> response = this.performRequest(url, probeVars.getParameters(), this.requestTemplate, this.requestMethod, this.requestHeaders);
            result.setResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.afterRequest();
        this.runChecks(result, probeVars.getChecksVars());

    }

    @Override
    public void runChecks(ProbeResult result, List<CheckVarsDTO> checksVars) {
        log.info("{}", result.isSuccess());
        
        for (CheckVarsDTO checkVars : checksVars) {
            Check check = CheckFactory.getProbe(checkVars.getCheckClass());
            check.perform(result, checkVars);

        }
        
        
        
    };
}
