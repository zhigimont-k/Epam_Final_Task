package by.epam.web.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator {
    private static NumberValidator instance = new NumberValidator();
    private static final String ID_FORMAT = "\\d{1,11}";
    private static final String MONEY_FORMAT = "\\d{1,6}";
    private static final int MIN_ID = 1;
    private static final int MIN_MONEY = 1;

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
        return intId >= MIN_ID;
    }

    public boolean validateMoney(String money){
        Matcher matcher = Pattern.compile(MONEY_FORMAT).matcher(money);
        if (!matcher.matches()){
            return false;
        }
        int intId = Integer.parseInt(money);
        return intId >= MIN_MONEY;
    }
}
