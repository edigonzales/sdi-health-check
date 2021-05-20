package ch.so.agi.healthcheck;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class Result {
    protected String className; 

    protected Boolean success;
    
    protected String message;
    
    protected Instant startTime;
    
    protected Instant endTime;
    
    protected double responseTimeSecs = -1;
    
    protected List<Result> results = new ArrayList<Result>();
    
    protected List<Result> resultsFailed = new ArrayList<Result>();
    
    protected Map<String,Object> reportMap;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public double getResponseTimeSecs() {
        return responseTimeSecs;
    }

    public void setResponseTimeSecs(double responseTimeSecs) {
        this.responseTimeSecs = responseTimeSecs;
    }
    
    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void addResult(Result result) {
        this.results.add(result);
                
        if (result.success != null && !result.success) {
            this.success = false;
            this.resultsFailed.add(result);
            this.message = this.resultsFailed.get(0).getMessage();
        } else {
            this.success = true;
            this.message = "OK";
        }
        
        if (result.getResponseTimeSecs() >= 0) {
            if (this.responseTimeSecs == -1) {
                this.responseTimeSecs = 0;
            }
            this.responseTimeSecs += result.getResponseTimeSecs();
        }
    }
     
    public void start() {
        this.startTime = Instant.now();
    }
    
    public void stop() {
        this.endTime = Instant.now();
        
        this.responseTimeSecs = Duration.between(startTime, endTime).toSeconds();
    }    
}
