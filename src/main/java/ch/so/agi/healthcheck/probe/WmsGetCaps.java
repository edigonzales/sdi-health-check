package ch.so.agi.healthcheck.probe;

import java.io.IOException;
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
import ch.so.agi.healthcheck.check.CheckResult;
import ch.so.agi.healthcheck.check.Check;
import ch.so.agi.healthcheck.model.CheckVars;
import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.model.ProbeVarsDTO;
import ch.so.agi.healthcheck.model.ResourceDTO;

@Service
// TODO Beschreibung etc. als Annotation?
public class WmsGetCaps extends Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    @ParamDefinition(name = "service", description = "The WMS service within resource endpoint")
    @ParamDefinition(name = "version", description = "The WMS service version within resource endpoint")
    private String requestTemplate = "?SERVICE=${service}&VERSION=${version}&REQUEST=GetCapabilities";

    @Override
    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }
    
    @Override
    public String getRequestTemplate() {
        return requestTemplate;
    }
   
}
