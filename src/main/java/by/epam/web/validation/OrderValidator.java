package by.epam.web.validation;

import by.epam.web.entity.Order;
import by.epam.web.entity.User;

public class OrderValidator implements AbstractValidator<Order> {
    @Override
    public boolean validate(Order entity) {
        return false;
    }

    public boolean checkAccess(User user, Order entity) {
        return false;
    }
}
