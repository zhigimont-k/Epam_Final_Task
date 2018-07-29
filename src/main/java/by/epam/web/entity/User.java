package by.epam.web.entity;

import java.util.Objects;

public class User extends Entity {
    public enum Status {
        USER("user"), ADMIN("admin"), BANNED("banned");
        private String statusName;
        Status(String statusName){
            this.statusName = statusName;
        }
        public String getName(){
            return statusName;
        }
    }

    private int id;
    private String login;
    private String password;
    private String userName;
    private String email;
    private String phoneNumber;
    private Status status;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status.getName();
    }

    public Status getUserStatus(){
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        for (Status st : Status.values()) {
            if (st.name().equalsIgnoreCase(status)) {
                this.status = st;
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                status == user.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, login, password, userName, email, phoneNumber, status);
    }
}
