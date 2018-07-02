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

    public Command initCommand(String commandName) throws UnknownCommandException{
        Command command;
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
            default: throw new UnknownCommandException("Unknown command");
        }
        return command;
    }
}
