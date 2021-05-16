package ch.so.agi.healthcheck.check;

public class WmsLayer {
    private String name;
    private String title;
    private String parentName;
    private String parentTitle;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getParentTitle() {
        return parentTitle;
    }
    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }
    @Override
    public String toString() {
        return "WmsLayer [name=" + name + ", title=" + title + ", parentName=" + parentName + ", parentTitle="
                + parentTitle + "]";
    }
}
