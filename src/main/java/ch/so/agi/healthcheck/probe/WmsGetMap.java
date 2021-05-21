package ch.so.agi.healthcheck.probe;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.Builder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.ProbeVarsDTO;
import ch.so.agi.healthcheck.model.ResourceDTO;

@Service
public class WmsGetMap extends Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    @ParamDefinition(name = "format", description = "The image format")
    @ParamDefinition(name = "transparent", description = "Transparent image")
    @ParamDefinition(name = "layers", description = "The WMS layers")
    @ParamDefinition(name = "styles", description = "The styles to apply")
    @ParamDefinition(name = "crs", description = "The SRS as EPSG: code")
    @ParamDefinition(name = "width", description = "The image width")
    @ParamDefinition(name = "height", description = "The image height")
    @ParamDefinition(name = "bbox", description = "The WMS bounding box")
    private String requestTemplate = "?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=${format}"
            + "&TRANSPARENT=${transparent}&LAYERS=${layers}&STYLES=${styles}&CRS=${crs}"
            + "&WIDTH=${width}&HEIGHT=${height}&BBOX=${bbox}";
        
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
