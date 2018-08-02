package by.epam.web.util.mail;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailSenderThread extends Thread {
    private static Logger logger = LogManager.getLogger();
    private static final String FROM_MAIL = "catbeautysalonmeow@gmail.com";
    private String mailTo;
    private String mailFrom;
    private String mailSubject;
    private String mailContent;
    private Session mailSession;

    public MailSenderThread(String mailTo, String mailSubject, String mailContent) {
        this.mailTo = mailTo;
        this.mailFrom = FROM_MAIL;
        this.mailSubject = mailSubject;
        this.mailContent = mailContent;
    }

    private void buildMailSession() {
        Properties props = new Properties();
        props.put(Settings.SMTP_AUTH, "true");
        props.put(Settings.PROTOCOL, Settings.PROTOCOL_VALUE);
        props.put(Settings.HOST, Settings.HOST_VALUE);
        props.put(Settings.USER, "true");
        props.put(Settings.SEND_PARTIAL, "true");
        props.put(Settings.START_TLS_ENABLE, "true");
        mailSession=Session.getDefaultInstance(props);
        mailSession.setDebug(true);
    }

    @Override
    public void run() {
        try {
            buildMailSession();
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            message.setSubject(mailSubject);
            message.setContent(mailContent, "text/html; charset=UTF-8");
            message.setSentDate(new Date());
            Transport transport = mailSession.getTransport();
            transport.connect(Settings.HOST_VALUE, Settings.DEFAULT_PORT,
                    Settings.USER_VALUE, Settings.PASSWORD_VALUE);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            logger.log(Level.INFO, "Message sent");
        } catch (MessagingException e) {
            logger.log(Level.ERROR, e);
        }
    }
}
