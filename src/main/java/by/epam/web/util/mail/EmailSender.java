package by.epam.web.util.mail;

import java.io.File;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class EmailSender {

    EmailSender() {
    }

    private void sendMail() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", MailManager.getHost());
        prop.put("mail.smtp.port", MailManager.getPort());
        prop.put("mail.smtp.ssl.trust", MailManager.getHost());

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailManager.getUsername(), MailManager.getPassword());
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailManager.getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("to@gmail.com"));
            message.setSubject("Mail Subject");

            String msg = "This is my first email using JavaMailer";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File("pom.xml"));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    public static void main(String ... args) {
//        new EmailService("smtp.mailtrap.io", 25, "87ba3d9555fae8", "91cb4379af43ed");
//    }

}
