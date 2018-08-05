package by.epam.web.validation;

import by.epam.web.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static UserValidator instance = new UserValidator();
    private static final String LOGIN_FORMAT = "[\\w^_]{4,20}";
    private static final String PASSWORD_FORMAT = "[\\w^_]{6,32}";
    private static final String EMAIL_FORMAT =
            "([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})";
    private static final int MAX_EMAIL_LENGTH = 50;
    private static final String PHONE_NUMBER_FORMAT = "\\+(\\d{12})";
    private static final String USER_NAME_FORMAT = "\\p{L}{2,40}";
    private static final String CARD_NUMBER_FORMAT = "\\d{16}";

    private UserValidator(){}

    public static UserValidator getInstance(){
        return instance;
    }

    public boolean validateLogin(String login){
        Matcher matcher = Pattern.compile(LOGIN_FORMAT).matcher(login);
        return matcher.matches();
    }

    public boolean validatePassword(String password){
        Matcher matcher = Pattern.compile(PASSWORD_FORMAT).matcher(password);
        return matcher.matches();
    }

    public boolean validateEmail(String email){
        Matcher matcher = Pattern.compile(EMAIL_FORMAT).matcher(email);
        return matcher.matches() && email.length() <= MAX_EMAIL_LENGTH;
    }

    public boolean validatePhoneNumber(String phoneNumber){
        Matcher matcher = Pattern.compile(PHONE_NUMBER_FORMAT).matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean validateUserName(String userName){
        Matcher matcher = Pattern.compile(USER_NAME_FORMAT).matcher(userName);
        return matcher.matches();
    }

    public boolean validateCardNumber(String cardNumber){
        Matcher matcher = Pattern.compile(CARD_NUMBER_FORMAT).matcher(cardNumber);
        return matcher.matches();
    }

    public boolean validateStatus(String status){
        return User.Status.BANNED.getName().equals(status) ||
                User.Status.USER.getName().equals(status) ||
                User.Status.ADMIN.getName().equals(status);
    }
}
