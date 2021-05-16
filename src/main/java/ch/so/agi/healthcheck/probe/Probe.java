package ch.so.agi.healthcheck.probe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.Builder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.CheckVars;
import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.model.ProbeVarsDTO;
import ch.so.agi.healthcheck.model.ResourceDTO;

public interface Probe {
    final Logger log = LoggerFactory.getLogger(Probe.class);
      
    default void beforeRequest() {
        
    };
    
    default HttpResponse<InputStream> performRequest(String resourceUrl, String requestParameters, String requestTemplate,
            String requestMethod, Map<String, String> requestHeaders) throws IOException, InterruptedException {

        Map<String, Object> requestParamsMap = null;
        try {
            requestParamsMap = new ObjectMapper().readValue(requestParameters, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        
        StringSubstitutor sub = new StringSubstitutor(requestParamsMap);
        String resolvedRequestTemplate = sub.replace(requestTemplate);
                
        String requestUrl = resourceUrl + resolvedRequestTemplate;
        log.info(requestUrl);
        

            HttpClient httpClient = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .followRedirects(Redirect.ALWAYS)
                    .build();
            
            if (requestMethod.equalsIgnoreCase("GET")) {                
                Builder requestBuilder = HttpRequest.newBuilder();
                requestBuilder
                    .GET()
                    .uri(URI.create(requestUrl));
                
                Map<String, String> standardRequestHeaders = new HashMap<String, String>() {{
                    put("User-Agent", "SdiHealthCheck");
                    put("Accept-Encoding", "deflate, gzip;q=1.0, *;q=0.5");
                }};
                 
                for (Map.Entry<String, String> entry : standardRequestHeaders.entrySet()) {
                    requestBuilder.setHeader(entry.getKey(), entry.getValue());
                }
                
                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    requestBuilder.setHeader(entry.getKey(), entry.getValue());
                }
                
                HttpRequest request = requestBuilder.build();

                HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
                
//                System.out.println(response.body());
//                System.out.println(response.statusCode());
//
//                HttpHeaders headers = response.headers();
//                headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
                
                return response;
            }                
        return null;
    }
   
    default void afterRequest() {};
   
    public void runChecks(ProbeResult result, List<CheckVarsDTO> checksVars);
    
    public ProbeResult run(ResourceDTO resource, ProbeVarsDTO probeVars);
//    default void run(String url, ProbeVarsDTO probeVars) {
//        log.info(url);
//        log.info(requestParameters);
//        
//        this.beforeRequest();
//        this.performRequest(url, requestParameters, getRequestTemplate());
//        this.afterRequest();
        
//        this.runChecks();
//    }
}
