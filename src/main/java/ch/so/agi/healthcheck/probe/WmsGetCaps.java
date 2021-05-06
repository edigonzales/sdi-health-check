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

@Service
public class WmsGetCaps implements IProbe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    @ParamDefinition(name = "service", description = "The WMS service within resource endpoint")
    @ParamDefinition(name = "version", description = "The WMS service version within resource endpoint")
    private String requestTemplate = "?SERVICE=${service}&VERSION=${version}&REQUEST=GetCapabilities";
    
    // TODO: mit getXXXXXX im Interface könnte man es wohl schon noch so machen, dass man run nicht
    // zu implementieren braucht im Regelfall.
    @Override
    public void run(String url, String requestParameters) {
        ProbeResult result = new ProbeResult();
        
        this.beforeRequest();
        try {
            HttpResponse<InputStream> response = this.performRequest(url, requestParameters, this.requestTemplate, this.requestMethod, this.requestHeaders);
            result.setResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.afterRequest();
        this.runChecks(result);

    }

    @Override
    public void runChecks(ProbeResult result) {
        log.info("{}", result.isSuccess());
    };
}
