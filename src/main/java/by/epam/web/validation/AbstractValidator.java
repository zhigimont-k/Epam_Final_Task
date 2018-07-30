package by.epam.web.validation;

import by.epam.web.entity.Entity;

public interface AbstractValidator<T extends Entity> {
    int MIN_ID_VALUE = 1;

    boolean validate(T entity);

    default boolean validateId(int id) {
        return id >= MIN_ID_VALUE;
    }
}
