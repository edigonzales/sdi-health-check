package ch.so.agi.healthcheck.check;

import java.io.IOException;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class ContainsStrings extends Check {

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
