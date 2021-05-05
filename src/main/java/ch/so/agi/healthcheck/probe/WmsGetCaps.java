package ch.so.agi.healthcheck.probe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WmsGetCaps extends Probe implements IProbe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String requestTemplate = "?SERVICE=${service}&VERSION=${version}&REQUEST=GetCapabilities";
      
    private List<ParamDef> paramDefs = Arrays.asList(
            new ParamDef("service", "java.lang.String", "The WMS service within resource endpoint"),
            new ParamDef("version", "java.lang.String", "The WMS service version within resource endpoint")
            );

    // TODO:
    // Interface und keine abstrakte Klasse. performRequest ist immer gleich (> Util?). 
    // Requesttemplate wüsste man nicht, dass es das geben muss? Schlimm? Zusammenhang mit
    // performrequest?
    // Annotation für Meta? Kann man dann mit Reflecteion auslesen. 
    // Dann kann man auch die Parameter so nenne wie man will. In der Annotation muss eventuell
    // einfach klar sein, um was es sich handelt, e.g. @JsonElement(key = "personAge")

    public WmsGetCaps() {

    }
    
    
    public WmsGetCaps(String requestTemplate4, List<ParamDef> paramDefs, Map<String, String> requestHeaders) {
//        super(this.requestTemplate, null, null);
//        super.setRequestTemplate(this.requestTemplate);
//        super.setParamDefs(paramDefs);
//      super.setRequestMethod(null);
    }


    @Override
    public void foo() {
        // TODO Auto-generated method stub
        
    }
    
//    public WmsGetCaps(Resource resource, ProbeVars probeVars) {
//        super(resource, probeVars);
//        super.setRequestTemplate(this.requestTemplate);
//        super.setParamDefs(paramDefs);
//    }

//    @Override
//    public void performRequest() {
//        log.info("performRequest");
//    }
    
//    @Override
//    public void run(String resourceUrl, String requestParameters) {
//        log.info("fubar");
//    }
}
