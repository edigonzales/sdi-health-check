package ch.so.agi.healthcheck;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Result {
    protected boolean success = true;
    
    protected String message;
    
    private Instant startTime;
    
    private Instant endTime;
    
    protected long responseTimeSecs = -1;
    
    protected List<Result> results = new ArrayList<Result>();
    
    protected List<Result> resultsFailed = new ArrayList<Result>();
    
    protected Map<String,Object> reportMap = new HashMap<>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getResponseTimeSecs() {
        return responseTimeSecs;
    }

    public void setResponseTimeSecs(long responseTimeSecs) {
        this.responseTimeSecs = responseTimeSecs;
    }
    
    public void addResult(Result result) {
        this.results.add(result);
        
        if (!result.success) {
            this.success = false;
            this.resultsFailed.add(result);
            this.message = this.resultsFailed.get(0).getMessage();
        }
    }
    
    public void start() {
        this.startTime = Instant.now();
    }
    
    public void stop() {
        this.endTime = Instant.now();
        
        this.responseTimeSecs = Duration.between(startTime, endTime).toSeconds();
    }
    
    public abstract String getReport();
    
    public abstract Map<String,Object> getRawReport();
}
