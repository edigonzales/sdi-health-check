package ch.so.agi.healthcheck.check;

import java.io.IOException;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class NotContainsStrings extends Check {

    @Override
    public String getName() {
        return "Response NOT contains strings";
    }

    @Override
    public String getDescription() {
        return "HTTP response does not contain any of the (comma-separated) strings specified";
    }

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Performing: " + this.getClass().getCanonicalName());
        
    }

}
