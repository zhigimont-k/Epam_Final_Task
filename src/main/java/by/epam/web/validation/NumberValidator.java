package by.epam.web.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator {
    private static NumberValidator instance = new NumberValidator();
    private static final String ID_FORMAT = "\\d{1,11}";
    private static final String MONEY_FORMAT = "\\d{1,6}";
    private static final String PAGE_PARAMETER_FORMAT = "\\d{1,5}";
    private static final int MIN_NUMBER = 1;

    private NumberValidator(){}

    public static NumberValidator getInstance(){
        return instance;
    }

    public boolean validateId(String id){
        Matcher matcher = Pattern.compile(ID_FORMAT).matcher(id);
        if (!matcher.matches()){
            return false;
        }
        int intId = Integer.parseInt(id);
        return intId >= MIN_NUMBER;
    }

    public boolean validateMoney(String money){
        Matcher matcher = Pattern.compile(MONEY_FORMAT).matcher(money);
        if (!matcher.matches()){
            return false;
        }
        int intId = Integer.parseInt(money);
        return intId >= MIN_NUMBER;
    }

    public boolean validatePageParameter(String value){
        Matcher matcher = Pattern.compile(PAGE_PARAMETER_FORMAT).matcher(value);
        if (!matcher.matches()){
            return false;
        }
        int intId = Integer.parseInt(value);
        return intId >= MIN_NUMBER;
    }
}
