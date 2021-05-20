package ch.so.agi.healthcheck.check;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class HttpStatusNoError extends Check {

    @Override
    public void perform(CheckVarsDTO checkVars) {
        log.info("Performing: " + this.getClass().getCanonicalName());
        
        int statusCode = this.probe.getResponse().statusCode();
        if (statusCode >= 400 && statusCode < 600) {            
            this.setResult(false, "HTTP Error status="+String.valueOf(statusCode));
        } else {
            this.setResult(true, null);
        }
    }

    @Override
    public String getName() {
        return "HTTP status should not be errored";
    }

    @Override
    public String getDescription() {
        return "Response should not contain a HTTP 400 or 500 range Error";
    }
}
