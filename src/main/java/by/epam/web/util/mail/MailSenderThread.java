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
    private static final Logger logger = LogManager.getLogger();
    private String mailTo;
    private String mailFrom;
    private String mailSubject;
    private String mailContent;
    private Session mailSession;
    private Settings settings;

    public MailSenderThread(String mailTo, String mailSubject, String mailContent, Settings settings) {
        this.mailTo = mailTo;
        this.mailFrom = "catbeautysalonmeow@gmail.com";
        this.mailSubject = mailSubject;
        this.mailContent = mailContent;
        this.settings = settings;
    }

    private void buildMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put(Settings.PROTOCOL, settings.getProtocolValue());
        props.put(Settings.HOST, settings.getHostValue());
        props.put(Settings.USER, "true");
        props.put(Settings.SEND_PARTIAL, "true");
        props.put("mail.smtp.starttls.enable", "true");
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
            transport.connect(settings.getHostValue(), settings.getPortValue(), settings.getUserValue(), settings.getPasswordValue());
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            logger.log(Level.INFO, "Sended");
        } catch (MessagingException e) {
            logger.log(Level.ERROR, e);
        }
    }
}
