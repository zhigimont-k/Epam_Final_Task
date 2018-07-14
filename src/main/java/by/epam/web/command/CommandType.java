package by.epam.web.command;

public enum CommandType {
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()), /*BAN_USER, UNBAN_USER, EDIT_USER,
    ADD_SERVICE, EDIT_SERVICE, REMOVE_SERVICE,
    ADD_ORDER, CHANGE_ORDER_STATUS, DEACTIVATE_ORDER,
    ADD_SERVICE_TO_ORDER, REMOVE_SERVICE_FROM_ORDER,
    ADD_REVIEW, EDIT_REVIEW, REMOVE_REVIEW*/;

    Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCurrentCommand() {
        return command;
    }
}
