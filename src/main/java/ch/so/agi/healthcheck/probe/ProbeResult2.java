package ch.so.agi.healthcheck.probe;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import ch.so.agi.healthcheck.check.CheckResult;

public class ProbeResult2 {
    private HttpResponse response;
    
    private boolean success = true;
    
    private long elapsedTime;
    
    private List<CheckResult> checkResults = new ArrayList<CheckResult>();

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public List<CheckResult> getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(List<CheckResult> checkResults) {
        this.checkResults = checkResults;
    }
    
    public void addCheckResult(CheckResult checkResult) {
        this.checkResults.add(checkResult);
    }
}