package by.epam.web.command;

import by.epam.web.command.admin.*;
import by.epam.web.command.common.ChangeLocaleCommand;
import by.epam.web.command.common.ViewActivitiesCommand;
import by.epam.web.command.user.*;
import by.epam.web.command.common.RegisterCommand;

public enum CommandType {
    REGISTER("register", new RegisterCommand()),
    LOGIN("login", new LoginCommand()),
    LOGOUT("logout", new LogoutCommand()),
    CHANGE_LOCALE("locale", new ChangeLocaleCommand()),
    VIEW_USERS("viewUsers", new ViewUsersCommand()),
    CHANGE_USER_STATUS("changeUserStatus", new ChangeUserStatusCommand()),
    ADD_ACTIVITY("addActivity", new AddActivityCommand()),
    UPDATE_ACTIVITY("updateActivity", new UpdateActivityCommand()),
    CHANGE_ACTIVITY_STATUS("changeActivityStatus", new ChangeActivityStatusCommand()),
    VIEW_ACTIVITIES("viewActivities", new ViewActivitiesCommand()),
    ADD_ORDER("addOrder", new AddOrderCommand()),
    CHANGE_ORDER_STATUS("changeOrderStatus", new ChangeOrderStatusCommand()),
    CANCEL_ORDER("cancelOrder", new CancelOrderCommand()),
    ADD_REVIEW("addReview", new AddReviewCommand()),
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
