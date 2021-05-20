package ch.so.agi.healthcheck.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.Probe;

public abstract class Check {
    final Logger log = LoggerFactory.getLogger(Check.class);

    protected Probe probe;
    
    private CheckResult result;
    
    public Check() {
        result = new CheckResult(this);
        result.start();
    }
    
    public void setProbe(Probe probe) {
        this.probe = probe;
    }
    
    public void setResult(boolean success, String message) {
        result.setMessage(message);
        result.setSuccess(success);
        result.stop();
    }
    
    public CheckResult getResult() {
        return this.result;
    } 
    
    public abstract void perform(CheckVarsDTO checkVars);
}
