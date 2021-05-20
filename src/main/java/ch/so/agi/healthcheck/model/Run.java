package ch.so.agi.healthcheck.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonType;

@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class)
})
@Entity
public class Run {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull
    private Date checkedDatetime;
    
    @NotNull
    private Boolean success;
    
    @NotNull
    private double responseTime;
    
    //@NotNull
    @Column(columnDefinition="TEXT")
    private String message;
    
    @NotNull
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String report;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Resource resource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCheckedDatetime() {
        return checkedDatetime;
    }

    public void setCheckedDatetime(Date checkedDatetime) {
        this.checkedDatetime = checkedDatetime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
