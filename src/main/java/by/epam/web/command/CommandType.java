package by.epam.web.command;

import by.epam.web.command.admin.ChangeUserStatusCommand;
import by.epam.web.command.admin.ViewUsersCommand;
import by.epam.web.command.common.ChangeLocaleCommand;
import by.epam.web.command.user.LoginCommand;
import by.epam.web.command.user.LogoutCommand;
import by.epam.web.command.user.RegisterCommand;

public enum CommandType {
    REGISTER("register", new RegisterCommand()),
    LOGIN("login", new LoginCommand()),
    LOGOUT("logout", new LogoutCommand()),
    CHANGE_LOCALE("locale", new ChangeLocaleCommand()),
    VIEW_USERS("viewUsers", new ViewUsersCommand()),
    CHANGE_USER_STATUS("changeUserStatus", new ChangeUserStatusCommand()),
    /*ADD_SERVICE, EDIT_SERVICE, REMOVE_SERVICE,
    ADD_ORDER, CHANGE_ORDER_STATUS, DEACTIVATE_ORDER,
    ADD_SERVICE_TO_ORDER, REMOVE_SERVICE_FROM_ORDER,
    ADD_REVIEW, EDIT_REVIEW, REMOVE_REVIEW*/;

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
