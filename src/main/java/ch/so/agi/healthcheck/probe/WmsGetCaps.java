package ch.so.agi.healthcheck.probe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WmsGetCaps extends Probe {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // TODO: auch via Constructor machen.
    private String requestTemplate = "?SERVICE=${service}&VERSION=${version}&REQUEST=GetCapabilities";
      
    private List<ParamDef> paramDefs = Arrays.asList(
            new ParamDef("service", "java.lang.String", "The WMS service within resource endpoint"),
            new ParamDef("version", "java.lang.String", "The WMS service version within resource endpoint")
            );

    // TODO: alles mittels Constructor
    public WmsGetCaps() {
        super();
        super.setRequestTemplate(this.requestTemplate);
        super.setParamDefs(paramDefs);
//      super.setRequestMethod(null);
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
