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

public abstract class Probe implements IProbe {
    final Logger log = LoggerFactory.getLogger(Probe.class);
    
    public HttpRequest request;
    
    protected HttpResponse<?> response;
    
    protected ProbeResult probeResult;
    
    public void beforeRequest() {};
    
    public void performRequest(String resourceUrl, String requestParameters, String requestTemplate,
            String requestMethod, Map<String, String> requestHeaders) throws IOException, InterruptedException {

        String requestUrl = resourceUrl;
        if (requestParameters != null && requestParameters.trim().length() > 0) {
            Map<String, Object> requestParamsMap = null;
            requestParamsMap = new ObjectMapper().readValue(requestParameters, HashMap.class);
            
            StringSubstitutor sub = new StringSubstitutor(requestParamsMap);
            String resolvedRequestTemplate = sub.replace(requestTemplate);
            requestUrl += resolvedRequestTemplate;
        }
                
        HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.ALWAYS)
                .build();

        if (requestMethod.equalsIgnoreCase("GET")) {
            Builder requestBuilder = HttpRequest.newBuilder();
            requestBuilder.GET().uri(URI.create(requestUrl));

            Map<String, String> standardRequestHeaders = new HashMap<String, String>() {
                {
                    put("User-Agent", "SdiHealthCheck");
                    // FIXME
                    // Ich kann anscheinend nicht mit komprimierte Antworten
                    // umgehen. Ich bin nicht sicher, ob es bloss am InputStream
                    // liegt oder grundsätzlich mit dem HttpClient.
                    // Workaround: https://stackoverflow.com/questions/53502626/does-java-http-client-handle-compression
                    //put("Accept-Encoding", "deflate, gzip;q=1.0, *;q=0.5");
                }
            };

            for (Map.Entry<String, String> entry : standardRequestHeaders.entrySet()) {
                requestBuilder.setHeader(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                requestBuilder.setHeader(entry.getKey(), entry.getValue());
            }
            
            request = requestBuilder.build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
                
//                System.out.println(response.body());
//                System.out.println(response.statusCode());
//
                HttpHeaders headers = response.headers();
                //headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
                
        }                
    }
   
    public void afterRequest() {};
       
    public void runChecks(List<CheckVarsDTO> checksVars) throws IOException {
        for (CheckVarsDTO checkVars : checksVars) {
            Check check = CheckFactory.getCheck(checkVars.getCheckClass());
            check.setProbe(this);
            check.perform(checkVars);
                      
            probeResult.addResult(check.getResult());
        }
    };
   
    public void run(ResourceDTO resource, ProbeVarsDTO probeVars) throws IOException, InterruptedException {
        // performRequest kann einige Zeit in Anspruch nehmen. Das verwirrt eventuell wenn man
        // die responseTime der Probe anschaut. Diese setzt sich aus der Summe der responseTimes
        // sämtlicher Checks und dem Request der Probe zusammen.
        log.info("Performing: " + this.getClass().getCanonicalName());
        
        probeResult = new ProbeResult(this);
        probeResult.start();
        
        this.beforeRequest();
        this.performRequest(resource.getUrl(), probeVars.getParameters(), getRequestTemplate(), getRequestMethod(), getRequestHeaders());
        this.afterRequest();
        this.runChecks(probeVars.getChecksVars());
             
        probeResult.setRequest(request.uri().toASCIIString());
        probeResult.stop();        
    }
    
    public HttpResponse<?> getResponse() {
        return response;
    }
    
    public ProbeResult getProbeResult() {
        return probeResult;
    }
}
