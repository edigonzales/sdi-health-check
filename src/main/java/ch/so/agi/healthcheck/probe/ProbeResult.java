package ch.so.agi.healthcheck.probe;

import java.net.http.HttpResponse;

public class ProbeResult {
    private HttpResponse response;
    
    private boolean success = true;

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
}
