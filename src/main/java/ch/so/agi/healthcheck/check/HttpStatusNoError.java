package ch.so.agi.healthcheck.check;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.ProbeResult;

public class HttpStatusNoError implements Check {

    @Override
    public void perform(ProbeResult result, CheckVarsDTO checkVars) {
        log.info("do something checker");
        
        CheckResult checkResult = new CheckResult();
        checkResult.setClassName(this.getClass().getCanonicalName());
                
        int statusCode = result.getResponse().statusCode();
        if (statusCode >= 400 && statusCode < 600) {
            checkResult.setSuccess(false);
            checkResult.setMessage("HTTP Error status="+String.valueOf(statusCode));
        }
        
        result.addCheckResult(checkResult);
    }
}
