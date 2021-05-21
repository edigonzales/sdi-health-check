package ch.so.agi.healthcheck.check;

import java.io.IOException;

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
        log.info("Performing: " + this.getClass().getCanonicalName());

    }

}
