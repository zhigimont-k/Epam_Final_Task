package by.epam.web.controller.command;

public enum CommandType {
    REGISTER {
        {
            this.command = new RegisterCommand();
        }
    }, LOGIN {
        {
            this.command = new LoginCommand();
        }
    }/*, LOGOUT, BAN_USER,
    ADD_SERVICE, EDIT_SERVICE, REMOVE_SERVICE,
    ADD_ORDER, CHANGE_ORDER_STATUS, DEACTIVATE_ORDER,
    ADD_SERVICE_TO_ORDER, REMOVE_SERVICE_FROM_ORDER,
    ADD_REVIEW, EDIT_REVIEW, REMOVE_REVIEW*/;

    Command command;

    public Command getCurrentCommand() {
        return command;
    }
}
