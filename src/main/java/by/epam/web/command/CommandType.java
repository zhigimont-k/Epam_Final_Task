package by.epam.web.command;

import by.epam.web.command.admin.*;
import by.epam.web.command.common.ChangeLocaleCommand;
import by.epam.web.command.common.ViewActivitiesCommand;
import by.epam.web.command.common.ViewActivityCommand;
import by.epam.web.command.user.*;
import by.epam.web.command.common.RegisterCommand;
import by.epam.web.command.user.ViewUserOrdersCommand;

public enum CommandType {
    REGISTER("register", new RegisterCommand()),
    LOGIN("login", new LoginCommand()),
    RESET_PASSWORD("resetPassword", new ResetPasswordCommand()),
    LOGOUT("logout", new LogoutCommand()),

    CHANGE_LOCALE("locale", new ChangeLocaleCommand()),

    VIEW_USERS("viewUsers", new ViewUsersCommand()),
    CHANGE_USER_STATUS("changeUserStatus", new ChangeUserStatusCommand()),
    UPDATE_USER("updateUser", new UpdateUserCommand()),

    ADD_ACTIVITY("addActivity", new AddActivityCommand()),
    EDIT_ACTIVITY("editActivity", new EditActivityCommand()),
    UPDATE_ACTIVITY("updateActivity", new UpdateActivityCommand()),
    VIEW_ACTIVITY("viewActivity", new ViewActivityCommand()),
    VIEW_ACTIVITIES("viewActivities", new ViewActivitiesCommand()),

    CREATE_ORDER("createOrder", new CreateOrderCommand()),
    VIEW_ORDER("viewOrder", new ViewOrderCommand()),
    ADD_ORDER("addOrder", new AddOrderCommand()),
    CHANGE_ORDER_STATUS("changeOrderStatus", new ChangeOrderStatusCommand()),
    CANCEL_ORDER("cancelOrder", new CancelOrderCommand()),
    VIEW_USER_ORDERS("viewUserOrders", new ViewUserOrdersCommand()),
    VIEW_ALL_ORDERS("viewAllOrders", new ViewOrdersCommand()),

    ADD_REVIEW("addReview", new AddReviewCommand()),
    EDIT_REVIEW("editReview", new EditReviewCommand()),
    UPDATE_REVIEW("updateReview", new UpdateReviewCommand()),
    DELETE_REVIEW("deleteReview", new DeleteReviewCommand());

    private Command command;
    private String commandName;

    CommandType(String commandName, Command command) {
        this.command = command;
        this.commandName = commandName;
    }

    public Command getCommand() {
        return command;
    }
    public String getName(){
        return commandName;
    }
}
