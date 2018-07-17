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
    private long id;
    private BigDecimal price;
    private String name;
    private String description;
    private Status status;

    public Activity(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
