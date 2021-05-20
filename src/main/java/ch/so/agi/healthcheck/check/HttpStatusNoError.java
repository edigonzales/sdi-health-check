package ch.so.agi.healthcheck.check;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class HttpStatusNoError extends Check {

    @Override
    public void perform(CheckVarsDTO checkVars) {
        log.info("Performing: " + this.getClass().getCanonicalName());
        
        CheckResult checkResult = new CheckResult(this);
        
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        int statusCode = this.probe.getResponse().statusCode();
        if (statusCode >= 400 && statusCode < 600) {            
            this.setResult(false, "HTTP Error status="+String.valueOf(statusCode));
        } else {
            this.setResult(true, null);
        }

        
    }
}
