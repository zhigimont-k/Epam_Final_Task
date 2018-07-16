package by.epam.web.command.factory;

import by.epam.web.command.Command;
import by.epam.web.command.CommandType;

import java.util.Arrays;
import java.util.Optional;

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

    public Optional<Command> defineCommand(String commandName) {
        return Arrays.stream(CommandType.values())
                .filter(command -> command.getName().equalsIgnoreCase(commandName))
                .findFirst()
                .map(CommandType::getCurrentCommand);
    }
}
