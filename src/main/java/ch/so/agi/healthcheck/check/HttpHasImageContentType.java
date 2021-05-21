package ch.so.agi.healthcheck.check;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.ParamDefinition;

public class HttpHasImageContentType extends Check {
    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Check: " + this.getClass().getCanonicalName());

        HttpHeaders headers = this.probe.getResponse().headers();
        String headerName = "content-type";
        
        if (!headers.map().containsKey(headerName)) {
            this.setResult(false, "HTTP response has no header" + headerName);
        } else {
            headers.map().get(headerName).forEach(v -> {
                if (v.contains("image/")) {
                    this.setResult(true, "OK");
                    return;
                }
                this.setResult(false, "HTTP response header " + headerName + " has no image value.");
            });
        }
    }

    @Override
    public String getName() {
        return "HTTP response is image";
    }

    @Override
    public String getDescription() {
        return "HTTP response has image/* Content-Type";
    }

}
