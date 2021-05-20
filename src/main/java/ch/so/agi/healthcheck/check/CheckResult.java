package ch.so.agi.healthcheck.check;

import ch.so.agi.healthcheck.Result;

public class CheckResult extends Result {
    private Check check;
        
    public CheckResult(Check check) {
        this.check = check;
        this.className = check.getClass().getCanonicalName();
    }    
}
