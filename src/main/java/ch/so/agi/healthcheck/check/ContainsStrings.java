package ch.so.agi.healthcheck.check;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.ParamDefinition;

public class ContainsStrings extends Check {
    @ParamDefinition(name = "strings", description = "The string text(s) that should be contained in response (comma-separated)")
    private String strings;

    @Override
    public String getName() {
        return "Response contains strings";
    }

    @Override
    public String getDescription() {
        return "HTTP response contains all (comma-separated) strings specified";
    }

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Check: " + this.getClass().getCanonicalName());
        
        InputStream is = (InputStream) this.probe.getResponse().body();
        String responseText = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                  .lines()
                  .collect(Collectors.joining("\n"));
        
        Map<String, Object> paramsMap = null;
        paramsMap = new ObjectMapper().readValue(checkVars.getParameters(), HashMap.class);
        strings = (String) paramsMap.get("strings");
        
        this.setResult(true, "OK");        
        Arrays.asList(strings.split(",")).forEach(s -> {
            if (!responseText.contains(s)) {
                this.setResult(false, s+ " not in response text");
                return;
            }
        });
    }
}
