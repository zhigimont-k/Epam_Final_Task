package by.epam.web.util.mail;

import java.util.Map;

public class MailComposer {

    private static final String PASSWORD_RESET_MESSAGE_THEME = "CBB | Password reset";
    private static final String ORDER_REMINDER_MESSAGE_THEME = "CBB | Order reminder";

    private static final String PASSWORD_RESET_MESSAGE = "Someone requested a password reset" +
            " and used your email. If you have no account on CBB, please ignore this message.\n" +
            "New password is: %s";
    private static final String ORDER_REMINDER_MESSAGE = "Dear %s, you have an " +
            "upcoming order in CBB soon. Thank you for using our service. Have a nice day.";

    public static String getResetPasswordMessage(String newPassword){
        return String.format(PASSWORD_RESET_MESSAGE, newPassword);
    }

    public static String getResetPasswordMessageTheme(){
        return PASSWORD_RESET_MESSAGE_THEME;
    }

    public static String getOrderReminderMessage(String userLogin){
        return String.format(ORDER_REMINDER_MESSAGE, userLogin);
    }

    public static String getOrderReminderMessageTheme(){
        return ORDER_REMINDER_MESSAGE_THEME;
    }
}
