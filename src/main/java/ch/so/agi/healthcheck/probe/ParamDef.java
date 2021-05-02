package ch.so.agi.healthcheck.probe;

public class ParamDef {
    public String name;
    public String type;
    public String description;
    public String defaultValue;
    public boolean required = false;

    public ParamDef(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.defaultValue = "";
    }
    
    public ParamDef(String name, String type, String description, String defaultValue) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.defaultValue = defaultValue;
    }
    
    public ParamDef(String name, String type, String description, String defaultValue, boolean required) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.defaultValue = defaultValue;
        this.required = required;
    }
}
