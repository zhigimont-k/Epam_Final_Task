package by.epam.web.entity;

import java.math.BigDecimal;

public class Order extends Entity {
    public enum Status{
        PENDING_APPROVAL("pendingApproval"), CONFIRMED("confirmed"), CANCELLED("cancelled"), FINISHED("finished");
        private String name;
        Status(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }
    }

    private int id;
    private BigDecimal price;
    private int userId;
    private int activityId;
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getStatus() {
        return status.getName();
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
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", userId=" + userId +
                ", activityId=" + activityId +
                ", status=" + status +
                '}';
    }
}
