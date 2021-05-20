package ch.so.agi.healthcheck.probe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.Builder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.check.Check;
import ch.so.agi.healthcheck.check.CheckFactory;
import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.model.ProbeVarsDTO;
import ch.so.agi.healthcheck.model.ResourceDTO;

public abstract class Probe {
    final Logger log = LoggerFactory.getLogger(Probe.class);
    
    private String 
    
    protected HttpResponse<?> response;
    
    protected ProbeResult probeResult;
    
    public void beforeRequest() {};
    
    public void performRequest(String resourceUrl, String requestParameters, String requestTemplate,
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
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
                
//                System.out.println(response.body());
//                System.out.println(response.statusCode());
//
//                HttpHeaders headers = response.headers();
//                headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
                
            }                
    }
   
    public void afterRequest() {};
       
    public void runChecks(List<CheckVarsDTO> checksVars) {
        for (CheckVarsDTO checkVars : checksVars) {
            Check check = CheckFactory.getCheck(checkVars.getCheckClass());
            check.setProbe(this);
            check.perform(checkVars);
            
            probeResult.addResult(check.getResult());
        }
    };
    
    public void run(ResourceDTO resource, ProbeVarsDTO probeVars) throws IOException, InterruptedException  {};
//    default void run(String url, ProbeVarsDTO probeVars) {
//        log.info(url);
//        log.info(requestParameters);
//        
//        this.beforeRequest();
//        this.performRequest(url, requestParameters, getRequestTemplate());
//        this.afterRequest();
        
//        this.runChecks();
//    }
    
    public HttpResponse<?> getResponse() {
        return response;
    }
    
    public ProbeResult getProbeResult() {
        return probeResult;
    }
}
