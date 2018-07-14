package by.epam.web.command;

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
                .filter(command -> command.name().equalsIgnoreCase(commandName))
                .findFirst()
                .map(CommandType::getCurrentCommand);
    }
}