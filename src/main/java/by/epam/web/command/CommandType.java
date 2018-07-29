package by.epam.web.command;

import by.epam.web.command.admin.*;
import by.epam.web.command.common.ChangeLocaleCommand;
import by.epam.web.command.common.ViewActivitiesCommand;
import by.epam.web.command.common.ViewActivityCommand;
import by.epam.web.command.user.*;
import by.epam.web.command.common.RegisterCommand;
import by.epam.web.command.user.ViewUserOrdersCommand;
import by.epam.web.entity.User;

public enum CommandType {
    REGISTER("register", new RegisterCommand(), CommandRight.GUEST),
    LOGIN("login", new LoginCommand(), CommandRight.GUEST),
    RESET_PASSWORD("resetPassword", new ResetPasswordCommand(), CommandRight.GUEST),
    LOGOUT("logout", new LogoutCommand(), CommandRight.USER),

    CHANGE_LOCALE("locale", new ChangeLocaleCommand(), CommandRight.GUEST),

    VIEW_USERS("viewUsers", new ViewUsersCommand(), CommandRight.ADMIN),
    CHANGE_USER_STATUS("changeUserStatus", new ChangeUserStatusCommand(), CommandRight.ADMIN),
    UPDATE_USER("updateUser", new UpdateUserCommand(), CommandRight.USER),

    ADD_ACTIVITY("addActivity", new AddActivityCommand(), CommandRight.ADMIN),
    EDIT_ACTIVITY("editActivity", new EditActivityCommand(), CommandRight.ADMIN),
    UPDATE_ACTIVITY("updateActivity", new UpdateActivityCommand(), CommandRight.ADMIN),
    VIEW_ACTIVITY("viewActivity", new ViewActivityCommand(), CommandRight.GUEST),
    VIEW_ACTIVITIES("viewActivities", new ViewActivitiesCommand(), CommandRight.GUEST),

    CREATE_ORDER("createOrder", new CreateOrderCommand(), CommandRight.USER),
    VIEW_ORDER("viewOrder", new ViewOrderCommand(), CommandRight.USER),
    ADD_ORDER("addOrder", new AddOrderCommand(), CommandRight.USER),
    CHANGE_ORDER_STATUS("changeOrderStatus", new ChangeOrderStatusCommand(), CommandRight.ADMIN),
    CANCEL_ORDER("cancelOrder", new CancelOrderCommand(), CommandRight.USER),
    VIEW_USER_ORDERS("viewUserOrders", new ViewUserOrdersCommand(), CommandRight.USER),
    VIEW_ALL_ORDERS("viewAllOrders", new ViewOrdersCommand(), CommandRight.ADMIN),

    ADD_REVIEW("addReview", new AddReviewCommand(), CommandRight.USER),
    EDIT_REVIEW("editReview", new EditReviewCommand(), CommandRight.USER),
    UPDATE_REVIEW("updateReview", new UpdateReviewCommand(), CommandRight.USER),
    DELETE_REVIEW("deleteReview", new DeleteReviewCommand(), CommandRight.ADMIN);

    private Command command;
    private String commandName;
    private CommandRight commandRight;

    CommandType(String commandName, Command command, CommandRight commandRight) {
        this.command = command;
        this.commandName = commandName;
        this.commandRight = commandRight;
    }

    public Command getCommand() {
        return command;
    }
    public String getName(){
        return commandName;
    }
    public CommandRight getCommandRights(){ return commandRight; }
}

