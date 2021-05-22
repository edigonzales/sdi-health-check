package ch.so.agi.healthcheck.check;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.ParamDefinition;

public class HttpStatusMatch extends Check {
    @ParamDefinition(name = "statusCode", description = "The HTTP status code to match.")
    private String statusCode;

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Check: " + this.getClass().getCanonicalName());
        
        Map<String, Object> paramsMap = null;
        paramsMap = new ObjectMapper().readValue(checkVars.getParameters(), HashMap.class);
        statusCode = (String) paramsMap.get("statusCode");

        int serverStatusCode = this.probe.getResponse().statusCode();
        
        if (serverStatusCode == Integer.valueOf(statusCode)) {
            this.setResult(true, "OK");
            return;
        }
        
        this.setResult(false, "HTTP status " + String.valueOf(serverStatusCode) + " does not match expected status " + statusCode);
    }

    @Override
    public String getName() {
        return "HTTP status match.";
    }

    @Override
    public String getDescription() {
        return "Response must match specific HTTP status";
    }
}
