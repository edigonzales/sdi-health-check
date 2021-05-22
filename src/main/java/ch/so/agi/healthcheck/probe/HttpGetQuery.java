package ch.so.agi.healthcheck.probe;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpGetQuery extends Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    @ParamDefinition(name = "query", description = "The query")
    private String requestTemplate = "?${query}";

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
