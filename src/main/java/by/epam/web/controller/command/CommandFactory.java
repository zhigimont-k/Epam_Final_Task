package by.epam.web.controller.command;

public final class CommandFactory {
    private static CommandFactory instance;

    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }
        return instance;
    }

    public Command defineCommand(String commandName){ //return optional или emptycommand сделать статитечским полем
        Command command = new EmptyCommand();
        if (commandName == null || commandName.isEmpty()){
            return command;
        }
        switch (commandName) {
            case "register":
                command = new RegisterCommand();
                break;
            case "login":
                command = new LoginCommand();
                break;
            case "locale":
                command = new ChangeLocaleCommand();
                break;
            case "logout":
                command = new LogoutCommand();
                break;
        }
        return command;
    }
}
