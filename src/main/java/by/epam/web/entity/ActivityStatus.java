package by.epam.web.entity;

public enum ActivityStatus {
    AVAILABLE("available"), UNAVAILABLE("unavailable");
    private String statusName;
    ActivityStatus(String statusName){
        this.statusName = statusName;
    }
    public String getName(){
        return statusName;
    }
}
