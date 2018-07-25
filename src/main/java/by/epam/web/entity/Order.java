package by.epam.web.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Order extends Entity {
    public enum Status {
        PENDING_APPROVAL("pending_approval"), CONFIRMED("confirmed"), CANCELLED("cancelled"), FINISHED("finished");
        private String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private int id;
    private int userId;
    private List<Activity> activityList = new ArrayList<>();
    private Status status;
    private Timestamp dateTime;
    private BigDecimal price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int activityListSize() {
        return activityList.size();
    }

    public boolean activityListIsEmpty() {
        return activityList.isEmpty();
    }

    public boolean addActivity(Activity activity) {
        return activityList.add(activity);
    }

    public boolean removeActivity(Object o) {
        return activityList.remove(o);
    }

    public void sortActivityList(Comparator<? super Activity> c) {
        activityList.sort(c);
    }

    public Activity getActivity(int index) {
        return activityList.get(index);
    }

    public Activity setActivity(int index, Activity element) {
        return activityList.set(index, element);
    }

    public void addActivity(int index, Activity element) {
        activityList.add(index, element);
    }

    public Activity removeActivity(int index) {
        return activityList.remove(index);
    }

    public int indexOfActivity(Object o) {
        return activityList.indexOf(o);
    }

    public void forEach(Consumer<? super Activity> action) {
        activityList.forEach(action);
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

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityList=" + activityList +
                ", status=" + status +
                ", dateTime=" + dateTime +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                userId == order.userId &&
                Objects.equals(activityList, order.activityList) &&
                status == order.status &&
                Objects.equals(dateTime, order.dateTime) &&
                Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, activityList, status, dateTime, price);
    }
}
