package by.epam.web.util.mail;

import java.util.Map;

public class MailComposer {

    public enum MessageType {
        PASSWORD_RESET, ORDER_REMINDER
    }

    private static final String PASSWORD_RESET_MESSAGE_THEME = "CBB | Password reset";
    private static final String ORDER_REMINDER_MESSAGE_THEME = "CBB | Order reminder";

    private static final String PASSWORD_RESET_MESSAGE = "Someone requested a password reset" +
            " and used your email. If you have no account on CBB, please ignore this message.\n" +
            "New password is: %s";
    private static final String ORDER_REMINDER_MESSAGE = "Dear %s, you have an " +
            "upcoming order in CBB soon. Thank you for using our service. Have a nice day.";

    private Map<String, String> message;

    public Map<String, String> getMessage(MessageType type) {
        return message;
    }
}
