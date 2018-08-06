package by.epam.web.validation;

import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderValidator {
    private static OrderValidator instance = new OrderValidator();
    private static final String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT_REGEX = "\\d{2}:\\d{2}";
    private static final LocalTime MIN_TIME = LocalTime.parse("08:59");
    private static final LocalTime MAX_TIME = LocalTime.parse("18:01");

    private OrderValidator() {
    }

    public static OrderValidator getInstance() {
        return instance;
    }

    public boolean validateDate(String date) {
        boolean result;
        Matcher matcher = Pattern.compile(DATE_FORMAT_REGEX).matcher(date);
        if (!matcher.matches()) {
            result = false;
        } else {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);
            try {
                Date dateValue = format.parse(date);
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                calendar.add(Calendar.MONTH, +1);
                Date maxDate = calendar.getTime();
                result = dateValue.after(now) && dateValue.before(maxDate);

            } catch (ParseException e) {
                return false;
            }
        }
        return result;
    }

    public boolean validateTime(String time, String date) {
        boolean result;
        Matcher matcher = Pattern.compile(TIME_FORMAT_REGEX).matcher(time);
        if (!matcher.matches()) {
            result = false;
        } else {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);
            try {
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                Date dateValue = format.parse(date);
                LocalTime timeValue = LocalTime.parse(time);
                if (DateUtils.isSameDay(dateValue, now)) {
                    LocalTime timeNow = LocalTime.now();
                    result = timeValue.isAfter(timeNow) && timeValue.isBefore(MAX_TIME);
                } else {
                    result = timeValue.isAfter(MIN_TIME) && timeValue.isBefore(MAX_TIME);
                }
            } catch (ParseException e) {
                return false;
            }
        }
        return result;
    }

    public boolean validateStatus(String status) {
        return Order.Status.PENDING.getName().equalsIgnoreCase(status) ||
                Order.Status.CANCELLED.getName().equalsIgnoreCase(status) ||
                Order.Status.CONFIRMED.getName().equalsIgnoreCase(status) ||
                Order.Status.FINISHED.getName().equalsIgnoreCase(status);
    }

}
