package by.epam.web.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    private List<Integer> activityIdList = new ArrayList<>();
    private Status status;
    private Timestamp dateTime;

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

    public int size() {
        return activityIdList.size();
    }

    public boolean isEmpty() {
        return activityIdList.isEmpty();
    }

    public boolean add(Integer integer) {
        return activityIdList.add(integer);
    }

    public boolean remove(Object o) {
        return activityIdList.remove(o);
    }

    public Integer get(int index) {
        return activityIdList.get(index);
    }

    public Integer set(int index, Integer element) {
        return activityIdList.set(index, element);
    }

    public void add(int index, Integer element) {
        activityIdList.add(index, element);
    }

    public Integer remove(int index) {
        return activityIdList.remove(index);
    }

    public void forEach(Consumer<? super Integer> action) {
        activityIdList.forEach(action);
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityIdList=" + activityIdList +
                ", status=" + status +
                ", dateTime=" + dateTime +
                '}';
    }
}
