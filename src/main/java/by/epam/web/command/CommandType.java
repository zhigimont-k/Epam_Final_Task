package by.epam.web.command;

public enum CommandType {
    REGISTER("register", new RegisterCommand()),
    LOGIN("login", new LoginCommand()),
    LOGOUT("logout", new LogoutCommand()),
    CHANGE_LOCALE("locale", new ChangeLocaleCommand()),
    /*BAN_USER, UNBAN_USER, EDIT_USER,
    ADD_SERVICE, EDIT_SERVICE, REMOVE_SERVICE,
    ADD_ORDER, CHANGE_ORDER_STATUS, DEACTIVATE_ORDER,
    ADD_SERVICE_TO_ORDER, REMOVE_SERVICE_FROM_ORDER,
    ADD_REVIEW, EDIT_REVIEW, REMOVE_REVIEW*/;

    private Command command;
    private String commandName;

    CommandType(String commandName, Command command) {
        this.command = command;
        this.commandName = commandName;
    }

    public Command getCurrentCommand() {
        return command;
    }
    public String getName(){
        return commandName;
    }
}
