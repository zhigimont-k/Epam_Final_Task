package by.epam.web.validation;

import by.epam.web.entity.Entity;
import by.epam.web.entity.User;

public interface AbstractValidator<T extends Entity> {
    boolean validate(T entity);
}
