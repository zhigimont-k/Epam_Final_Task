package by.epam.web.validation;

import by.epam.web.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements AbstractValidator<User> {
    private static final int MIN_LOGIN_LENGTH = 10;
    private static final int MAX_LOGIN_LENGTH = 10;
    private static final String LOGIN_FORMAT = "";


    private static final int MIN_PASSWORD_LENGTH = 10;
    private static final int MAX_PASSWORD_LENGTH = 10;
    private static final String PASSWORD_FORMAT = "";

    private static final int MIN_EMAIL_LENGTH = 10;
    private static final int MAX_EMAIL_LENGTH = 10;
    private static final String EMAIL_FORMAT = "";

    private static final int PHONE_NUMBER_LENGTH = 13;
    private static final String PHONE_NUMBER_FORMAT = "";


    private static final int MIN_USER_NAME_LENGTH = 10;
    private static final int MAX_USER_NAME_LENGTH = 10;
    private static final String USER_NAME_FORMAT = "";
    @Override
    public boolean validate(User user) {
        return validateLogin(user.getLogin()) && validatePassword(user.getPassword()) &&
                validateEmail(user.getEmail()) && validatePhoneNumber(user.getPhoneNumber()) &&
                validateUserName(user.getUserName());
    }

    private boolean validateLogin(String login){
        Matcher matcher = Pattern.compile(LOGIN_FORMAT).matcher(login);
        return login.length() >= MIN_LOGIN_LENGTH && login.length() <= MAX_LOGIN_LENGTH
                && matcher.matches();
    }

    private boolean validatePassword(String password){
        Matcher matcher = Pattern.compile(PASSWORD_FORMAT).matcher(password);
        return password.length() >= MIN_PASSWORD_LENGTH &&
                password.length() <= MAX_PASSWORD_LENGTH
                && matcher.matches();
    }

    private boolean validateEmail(String login){
        Matcher matcher = Pattern.compile(LOGIN_FORMAT).matcher(login);
        return login.length() >= MIN_LOGIN_LENGTH && login.length() <= MAX_LOGIN_LENGTH
                && matcher.matches();
    }

    private boolean validatePhoneNumber(String login){
        Matcher matcher = Pattern.compile(LOGIN_FORMAT).matcher(login);
        return login.length() >= MIN_LOGIN_LENGTH && login.length() <= MAX_LOGIN_LENGTH
                && matcher.matches();
    }

    private boolean validateUserName(String login){
        Matcher matcher = Pattern.compile(LOGIN_FORMAT).matcher(login);
        return login.length() >= MIN_LOGIN_LENGTH && login.length() <= MAX_LOGIN_LENGTH
                && matcher.matches();
    }




}
