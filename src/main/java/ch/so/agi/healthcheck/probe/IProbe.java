package ch.so.agi.healthcheck.probe;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IProbe {    
    public Map<String, String> getRequestHeaders();
    
    public String getRequestMethod();
    
    public String getRequestTemplate();
    
    public String getName();
    
    public String getDescription();
}
