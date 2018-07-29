package by.epam.web.util.mail;

class Settings {
    static final String HOST="mail.smtps.host";
    static final String PORT="mail.smtp.port";
    static final String USER="smtps.auth.user";
    static final String PASSWORD="smtps.auth.pass";
    static final String PROTOCOL="mail.transport.protocol";
    static final String SEND_PARTIAL="mail.smtp.sendpartial";
    static final String SMTP_AUTH = "mail.smtp.auth";
    static final String START_TLS_ENABLE = "mail.smtp.starttls.enable";

    static final String HOST_VALUE = "smtp.gmail.com";
    static final String USER_VALUE = "catbeautysalonmeow@gmail.com";
    static final String PASSWORD_VALUE = "cBBgf78ejndfgu#Q";
    static final String PROTOCOL_VALUE = "smtp";
    static final int DEFAULT_PORT = 587;


    private Settings(){

    }
}
