package ch.so.agi.healthcheck.probe;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public interface IProbe {
    final Logger log = LoggerFactory.getLogger(IProbe.class);
    
    public void runChecks(List<CheckVarsDTO> checksVars);
    
//    default void beforeRequest() {
//        
//    };
//    
//    default HttpResponse<InputStream> performRequest(String resourceUrl, String requestParameters, String requestTemplate,
//            String requestMethod, Map<String, String> requestHeaders) throws IOException, InterruptedException {
//
//        Map<String, Object> requestParamsMap = null;
//        try {
//            requestParamsMap = new ObjectMapper().readValue(requestParameters, HashMap.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//        
//        StringSubstitutor sub = new StringSubstitutor(requestParamsMap);
//        String resolvedRequestTemplate = sub.replace(requestTemplate);
//                
//        String requestUrl = resourceUrl + resolvedRequestTemplate;
//        log.info(requestUrl);
//        
//
//            HttpClient httpClient = HttpClient.newBuilder()
//                    .version(Version.HTTP_1_1)
//                    .followRedirects(Redirect.ALWAYS)
//                    .build();
//            
//            if (requestMethod.equalsIgnoreCase("GET")) {                
//                Builder requestBuilder = HttpRequest.newBuilder();
//                requestBuilder
//                    .GET()
//                    .uri(URI.create(requestUrl));
//                
//                Map<String, String> standardRequestHeaders = new HashMap<String, String>() {{
//                    put("User-Agent", "SdiHealthCheck");
//                    put("Accept-Encoding", "deflate, gzip;q=1.0, *;q=0.5");
//                }};
//                 
//                for (Map.Entry<String, String> entry : standardRequestHeaders.entrySet()) {
//                    requestBuilder.setHeader(entry.getKey(), entry.getValue());
//                }
//                
//                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
//                    requestBuilder.setHeader(entry.getKey(), entry.getValue());
//                }
//                
//                HttpRequest request = requestBuilder.build();
//
//                HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
//                
////                System.out.println(response.body());
////                System.out.println(response.statusCode());
////
////                HttpHeaders headers = response.headers();
////                headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
//                
//                return response;
//            }                
//        return null;
//    }
//   
//    default void afterRequest() {};
   
//    public void runChecks(ProbeResult2 result, List<CheckVarsDTO> checksVars);
//    
//    public ProbeResult2 run(ResourceDTO resource, ProbeVarsDTO probeVars);
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
