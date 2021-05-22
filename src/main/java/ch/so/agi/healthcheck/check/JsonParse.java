package ch.so.agi.healthcheck.check;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class JsonParse extends Check {

    @Override
    public String getName() {
        return "Valid JSON response";
    }

    @Override
    public String getDescription() {
        return "HTTP response contains valid JSON";
    }

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Check: " + this.getClass().getCanonicalName());

        InputStream is = (InputStream) this.probe.getResponse().body();
          
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(is);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            this.setResult(false, e.getMessage());
        } 

        this.setResult(true, "OK");
    }
}
