package by.epam.web.util.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

public class MailManager {
    private static final Logger logger = LogManager.getLogger();

    private static final String BASE_NAME = "mail";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_USER_NAME = "mail.user.name";
    private static final String MAIL_USER_PASSWORD = "mail.user.password";

    private static String host;
    private static int port;
    private static String username;
    private static String password;

    public MailManager(){
        readProperties();
    }


    private void readProperties(){
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME);
        if (bundle == null){
            logger.fatal("Couldn't process mail property file");
            throw new RuntimeException("Couldn't process mail property file");
        }
        host = bundle.getString(MAIL_SMTP_HOST);
        port = Integer.parseInt(bundle.getString(MAIL_SMTP_PORT));
        username = bundle.getString(MAIL_USER_NAME);
        password = bundle.getString(MAIL_USER_PASSWORD);
        if (host == null || port == 0 || username == null || password == null){
            logger.fatal("Not enough data to init mail manager");
            throw new RuntimeException("Not enough data to init mail manager");
        }
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
