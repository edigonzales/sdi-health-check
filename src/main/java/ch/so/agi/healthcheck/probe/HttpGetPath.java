package ch.so.agi.healthcheck.probe;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpGetPath extends Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    @ParamDefinition(name = "path", description = "The path")
    private String requestTemplate = "/${path}";

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

    @Override
    public String getName() {
        return "HTTP GET Resource URL with a path";
    }

    @Override
    public String getDescription() {
        return "HTTP GET Resource URL with /path string to be user-supplied (without /)";
    }

}
