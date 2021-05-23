package ch.so.agi.healthcheck.probe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.Builder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.ProbeVarsDTO;
import ch.so.agi.healthcheck.model.ResourceDTO;

@Service
public class WmsGetMapAll extends Probe {
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
 
    public void run(ResourceDTO resource, ProbeVarsDTO probeVars) throws IOException, InterruptedException {
        log.info("Performing: " + this.getClass().getCanonicalName());
        
        String capabilitiesUrl = resource.getUrl() + "?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetCapabilities";
        HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.ALWAYS)
                .build();

        Builder requestBuilder = HttpRequest.newBuilder();
        requestBuilder.GET().uri(URI.create(capabilitiesUrl));

        HttpRequest request = requestBuilder.build();
        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db;
        List<String> layers = new ArrayList<String>();
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(response.body());  

            doc.getDocumentElement().normalize();  
            NodeList nodeList = doc.getElementsByTagName("Layer"); 

            for (int itr = 0; itr < nodeList.getLength(); itr++) {  
                Node node = nodeList.item(itr);            
           
                if (node.getNodeType() == Node.ELEMENT_NODE) {  
                    Element eElement = (Element) node;
                    String name =  eElement.getElementsByTagName("Name").item(0).getTextContent();
                                        
                    // Layergruppen sollen nicht requestet werden, sondern
                    // nur die einzelnen Layer.
                    NodeList childLayerNodes = eElement.getElementsByTagName("Layer");
                    if (childLayerNodes.getLength() > 0) {
                        continue;
                    }
                    
                    layers.add(name);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }  

        probeResult = new ProbeResult(this);
        probeResult.start();

        // Layer-Parameter muss dynamisch den Parametern hinzugefügt werden. 
        Map<String, Object> requestParamsMap = null;
        requestParamsMap = new ObjectMapper().readValue(probeVars.getParameters(), HashMap.class);
                
        int i = 0;
        for (String layer : layers) {
            //requestParamsMap.put("layers", "ch.so.arp.nutzungsplanung.erschliessungsplanung.verkehrsflaechen");
            requestParamsMap.put("layers", layer);
            requestParamsMap.put("styles", "");            
            String params = new ObjectMapper().writeValueAsString(requestParamsMap);
            
            this.performRequest(resource.getUrl(), params, getRequestTemplate(), getRequestMethod(), getRequestHeaders());
            this.runChecks(probeVars.getChecksVars());
            
            probeResult.getResults().get(i).setRequest(this.request.uri().toASCIIString());
            
            i++;
            //if (i>1) break;
        }
        
        // Aufgrund der möglichen Grösse nur die Fehler behalten.
        probeResult.setResults(this.getProbeResult().getResultsFailed());
        probeResult.stop();      
    }

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
   
}
