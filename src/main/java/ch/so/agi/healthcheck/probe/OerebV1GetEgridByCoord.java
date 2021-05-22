package ch.so.agi.healthcheck.probe;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OerebV1GetEgridByCoord extends Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    @ParamDefinition(name = "format", description = "The output format")
    @ParamDefinition(name = "coord", description = "The location we're interested in. 'XY=easting,northing' or 'GNSS=lat,lon'")
    private String requestTemplate = "getegrid/${format}/?${coord}";

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
