package ch.so.agi.healthcheck.check;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.ParamDefinition;

public class HttpHasHeaderValue extends Check {
    @ParamDefinition(name = "headerName", description = "The HTTP header name")
    private String headerName;

    @ParamDefinition(name = "headerValue", description = "The HTTP header value")
    private String headerValue;

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Performing: " + this.getClass().getCanonicalName());

        Map<String, Object> paramsMap = null;
        paramsMap = new ObjectMapper().readValue(checkVars.getParameters(), HashMap.class);

        String headerName = (String) paramsMap.get("headerName");
        String headerValue = (String) paramsMap.get("headerValue");

        HttpHeaders headers = this.probe.getResponse().headers();
        headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

        if (!headers.map().containsKey(headerName)) {
            this.setResult(false, "HTTP response has no header" + headerName);
        } else {
            headers.map().get(headerName).forEach(v -> {
                if (v.contains(headerValue)) {
                    this.setResult(true, "OK");
                    return;
                }
                this.setResult(false, "HTTP response header " + headerName + " has no value " + headerValue);
            });
        }
    }

    @Override
    public String getName() {
        return "Has specific HTTP Header value";
    }

    @Override
    public String getDescription() {
        return "HTTP response has specific HTTP Header value";
    }

}
