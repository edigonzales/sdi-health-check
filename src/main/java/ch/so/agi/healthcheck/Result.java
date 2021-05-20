package ch.so.agi.healthcheck;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Result {
    protected Boolean success;
    
    protected String message;
    
    protected Instant startTime;
    
    protected Instant endTime;
    
    protected double responseTimeSecs = -1;
    
    protected List<Result> results = new ArrayList<Result>();
    
    protected List<Result> resultsFailed = new ArrayList<Result>();
    
    protected Map<String,Object> reportMap;

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

    public double getResponseTimeSecs() {
        return responseTimeSecs;
    }

    public void setResponseTimeSecs(double responseTimeSecs) {
        this.responseTimeSecs = responseTimeSecs;
    }
    
    public void addResult(Result result) {
        this.results.add(result);
                
        if (result.success != null && !result.success) {
            this.success = false;
            this.resultsFailed.add(result);
            this.message = this.resultsFailed.get(0).getMessage();
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
    
    public String getReport() {
        this.getRawReport();
        try {
            return new ObjectMapper().writeValueAsString(reportMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }

    };
    
    public abstract Map<String,Object> getRawReport();
}
