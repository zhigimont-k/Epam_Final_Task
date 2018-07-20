package by.epam.web.validation;

import by.epam.web.entity.User;

public class UserValidator implements AbstractValidator<User> {
    @Override
    public boolean validate(User entity) {
        return false;
    }
}
