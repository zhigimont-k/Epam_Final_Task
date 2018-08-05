package by.epam.web.validation;

import by.epam.web.entity.Activity;
import by.epam.web.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityValidator {
    private static ActivityValidator instance = new ActivityValidator();
    private static final String NAME_FORMAT = "\\p{L}{2,40}";
    private static final String DESCRIPTION_FORMAT = "[\\p{N}\\p{L}\\p{P}\\p{S}]{1,280}";
    private static final String PRICE_FORMAT = "\\d{1,10}";

    private ActivityValidator(){}

    public ActivityValidator getInstance(){
        return instance;
    }

    public boolean validateName(String name) {
        Matcher matcher = Pattern.compile(NAME_FORMAT).matcher(name);
        return matcher.matches();
    }

    public boolean validateDescription(String description){
        Matcher matcher = Pattern.compile(DESCRIPTION_FORMAT).matcher(description);
        return matcher.matches();
    }

    public boolean validateStatus(String status){
        return Activity.Status.AVAILABLE.getName().equalsIgnoreCase(status) ||
                Activity.Status.UNAVAILABLE.getName().equalsIgnoreCase(status);
    }

    public boolean validatePrice(String price){
        Matcher matcher = Pattern.compile(PRICE_FORMAT).matcher(price);
        return matcher.matches();
    }

}
