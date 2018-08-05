package by.epam.web.validation;

import by.epam.web.entity.Order;
import by.epam.web.entity.User;

public class OrderValidator {
    private static OrderValidator instance = new OrderValidator();

    private OrderValidator(){}

    public static OrderValidator getInstance(){
        return instance;
    }

    public boolean validateDate(String date){
        return false;
    }

    public boolean validateTime(String time){
        return false;
    }

}
