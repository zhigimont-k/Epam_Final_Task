package by.epam.web.validation;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderValidator {
    private static Logger logger = LogManager.getLogger();
    private static OrderValidator instance = new OrderValidator();
    private static final String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT_REGEX = "\\d{2}:\\d{2}";
    private static LocalTime minTime;
    private static LocalTime maxTime;
    private static final LocalTime DEFAULT_TIME = LocalTime.parse("00:00");
    private static final String BUNDLE_NAME = "cbb_info";
    private static ResourceBundle bundle;
    private static final String WORKING_HOURS_BEGIN_KEY = "cbb.working.hours.start";
    private static final String WORKING_HOURS_END_KEY = "cbb.working.hours.end";
    private static String workingHoursStart;
    private static String workingHoursEnd;

    private OrderValidator() {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        workingHoursStart = bundle.getString(WORKING_HOURS_BEGIN_KEY);
        workingHoursEnd = bundle.getString(WORKING_HOURS_END_KEY);
        if (validateWorkingHours(workingHoursStart) && validateWorkingHours(workingHoursEnd)){
            minTime = LocalTime.parse(workingHoursStart).minusSeconds(1);
            maxTime = LocalTime.parse(workingHoursEnd).plusSeconds(1);
        } else {
            minTime = DEFAULT_TIME;
            maxTime = DEFAULT_TIME;
        }
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
                    result = timeValue.isAfter(timeNow) && timeValue.isBefore(maxTime);
                } else {
                    result = timeValue.isAfter(minTime) && timeValue.isBefore(maxTime);
                }
            } catch (ParseException e) {
                return false;
            }
        }
        return result;
    }

    public boolean validateOrderTimeAfterNow(Timestamp timestamp){
        LocalDateTime orderTime = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        return orderTime.isAfter(now);
    }

    private boolean validateWorkingHours(String time) {
        try {
            LocalTime.parse(time);
            logger.log(Level.INFO, "Read time from property file: " + time);
            return true;
        } catch (DateTimeParseException | NullPointerException e) {
            logger.log(Level.INFO, "Invalid time: " + time);
            return false;
        }
    }

}
