package by.epam.web.validation;

import by.epam.web.entity.User;

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
    public boolean validate(User entity) {
        return false;
    }


}
