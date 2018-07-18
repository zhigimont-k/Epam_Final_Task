package by.epam.web.entity;

public enum UserStatus {
    USER("user"), ADMIN("admin"), BANNED("banned");
    private String statusName;
    UserStatus(String statusName){
        this.statusName = statusName;
    }
    public String getName(){
        return statusName;
    }
}
