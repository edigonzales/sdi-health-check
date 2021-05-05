package ch.so.agi.healthcheck.probe;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public abstract class Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestMethod = "GET";
    
    private Map<String, String> requestHeaders = new HashMap<String, String>();

    private Map<String, String> standardRequestHeaders = new HashMap<String, String>() {{
       put("User-Agent", "SdiHealthCheck");
       put("Accept-Encoding", "deflate, gzip;q=1.0, *;q=0.5");
    }};
    
    
    
    private String requestTemplate;
 
    private List<ParamDef> paramDefs;
            
    public Probe() {
        
    }
    
    public Probe(String requestTemplate, List<ParamDef> paramDefs, Map<String, String> requestHeaders) {
        this.requestTemplate = requestTemplate;
        this.paramDefs = paramDefs;
        this.requestHeaders = requestHeaders;
    }
    
    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String> getStandardRequestHeaders() {
        return standardRequestHeaders;
    }

    public void setStandardRequestHeaders(Map<String, String> standardRequestHeaders) {
        this.standardRequestHeaders = standardRequestHeaders;
    }

    public String getRequestTemplate() {
        return requestTemplate;
    }

    public void setRequestTemplate(String requestTemplate) {
        this.requestTemplate = requestTemplate;
    }

    public List<ParamDef> getParamDefs() {
        return paramDefs;
    }

    public void setParamDefs(List<ParamDef> paramDefs) {
        this.paramDefs = paramDefs;
    }
    
    public void beforeRequest() {
        
    }
    
    public void performRequest(String resourceUrl, String requestParameters) {
        log.info("base class: performRequets");
        log.info(requestTemplate);

        Map<String, Object> requestParamsMap = null;
        try {
            requestParamsMap = new ObjectMapper().readValue(requestParameters, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        
        StringSubstitutor sub = new StringSubstitutor(requestParamsMap);
        String resolvedRequestTemplate = sub.replace(this.requestTemplate);
                
        String requestUrl = resourceUrl + resolvedRequestTemplate;
        log.info(requestUrl);
        
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .followRedirects(Redirect.ALWAYS)
                    .build();
            
            if (this.requestMethod.equalsIgnoreCase("GET")) {                
                Builder requestBuilder = HttpRequest.newBuilder();
                requestBuilder
                    .GET()
                    .uri(URI.create(requestUrl));
                
                for (Map.Entry<String, String> entry : standardRequestHeaders.entrySet()) {
                    requestBuilder.setHeader(entry.getKey(), entry.getValue());
                }
                
                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    requestBuilder.setHeader(entry.getKey(), entry.getValue());
                }
                
                HttpRequest request = requestBuilder.build();

                HttpResponse<Path> response = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("/Users/stefan/tmp/fubar.data")));
                
                System.out.println(response.body());
                System.out.println(response.statusCode());

                HttpHeaders headers = response.headers();
                headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
                

                WebMapServer wms = null;
                try {
//                    System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//                    System.setProperty("sun.net.client.defaultConnectTimeout", "5000");

                  wms = new WebMapServer(new URL(requestUrl));
                } catch (Exception e) {
                    e.printStackTrace();
                } 


//                WMSCapabilities capabilities = wms.getCapabilities();
//                String serverName = capabilities.getService().getName();
//                String serverTitle = capabilities.getService().getTitle();
//                System.out.println(capabilities.getLayerList().size());
//                System.out.println(capabilities.getService());
//                for (Layer layer:  capabilities.getLayerList()) {
//                    System.out.println(layer.getName());
//                }
//                System.out.println("Capabilities retrieved from server: " + serverName + " (" + serverTitle + ")");
                System.out.println("fubar");
                
            }                
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void afterRequest() {
        
    }
    
    public void run(String resourceUrl, String requestParameters) {        
        this.beforeRequest();
        this.performRequest(resourceUrl, requestParameters);
        this.afterRequest();

        //this.runChecks();
    }
}
