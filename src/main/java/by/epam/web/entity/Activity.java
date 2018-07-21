package by.epam.web.entity;

import java.math.BigDecimal;

public class Activity extends Entity {
    public enum Status {
        AVAILABLE("available"), UNAVAILABLE("unavailable");
        private String statusName;
        Status(String statusName){
            this.statusName = statusName;
        }
        public String getName(){
            return statusName;
        }
    }
    private int id;
    private BigDecimal price;
    private String name;
    private String description;
    private Status status;

    public Activity(){}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus(){
        return status.name();
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

    public boolean isAvailable(){
        return Status.AVAILABLE.equals(status);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
