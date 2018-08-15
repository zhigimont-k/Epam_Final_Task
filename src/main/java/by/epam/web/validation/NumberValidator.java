package by.epam.web.validation;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator {
    private static NumberValidator instance = new NumberValidator();
    private static final String MONEY_FORMAT = "[1-9]{1,5}\\.?\\d{0,2}";
    private static final String PAGE_PARAMETER_FORMAT = "[1-9]\\d{1,5}";
    private static final int MIN_NUMBER = 1;

    private NumberValidator(){}

    public static NumberValidator getInstance(){
        return instance;
    }

    public boolean validateMoney(String money){
        Matcher matcher = Pattern.compile(MONEY_FORMAT).matcher(money);
        if (!matcher.matches()){
            return false;
        }
        return new BigDecimal(money).compareTo(BigDecimal.ONE) >= 0;
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
