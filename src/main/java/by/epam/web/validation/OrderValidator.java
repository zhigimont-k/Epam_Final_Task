package by.epam.web.validation;

import by.epam.web.entity.Order;

public class OrderValidator implements AbstractValidator<Order> {
    @Override
    public boolean validate(Order entity) {
        return false;
    }
}
