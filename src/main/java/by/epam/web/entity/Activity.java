package by.epam.web.entity;

import java.math.BigDecimal;

public class Activity extends Entity {
    private int id;
    private BigDecimal price;
    private String name;
    private String description;
    private String status;

    public Activity(){}

    public long getId() {
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
        return status;
    }

    public void setStatus(String status) {
        for (ActivityStatus st : ActivityStatus.values()) {
            if (st.name().equalsIgnoreCase(status)) {
                this.status = st.getName();
            }
        }
    }
}
