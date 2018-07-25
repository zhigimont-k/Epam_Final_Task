package by.epam.web.util.mail;

public class Settings {
    public static final String HOST="mail.smtps.host";
    public static final String PORT="mail.smtp.port";
    public static final String USER="smtps.auth.user";
    public static final String PASSWORD="smtps.auth.pass";
    public static final String PROTOCOL="mail.transport.protocol";
    public static final String SEND_PARTIAL="mail.smtp.sendpartial";

    private String hostValue;
    private int portValue;
    private String userValue;
    private String passwordValue;
    private String protocolValue;

    public String getHostValue() {
        return hostValue;
    }

    public void setHostValue(String hostValue) {
        this.hostValue = hostValue;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public int getPortValue() {
        return portValue;
    }

    public void setPortValue(int portValue) {
        this.portValue = portValue;
    }

    public String getProtocolValue() {
        return protocolValue;
    }

    public void setProtocolValue(String protocolValue) {
        this.protocolValue = protocolValue;
    }

    public String getUserValue() {
        return userValue;
    }

    public void setUserValue(String userValue) {
        this.userValue = userValue;
    }
}
