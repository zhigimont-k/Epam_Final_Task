package by.epam.web.validation;

import by.epam.web.entity.Entity;

public interface AbstractValidator<T extends Entity> {
    boolean validate(T entity);
}
