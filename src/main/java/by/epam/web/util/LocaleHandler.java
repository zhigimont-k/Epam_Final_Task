package by.epam.web.util;

import java.util.ResourceBundle;

public class LocaleHandler {
    private static final String BASE_NAME = "locale.locale";
    private static ResourceBundle bundle;
    private static final String WELCOME_MESSAGE = "locale.user.text.welcome";
    private static String lang;


    public static void setLanguage(String lang){
        LocaleHandler.lang = lang;
    }
    public static String getErrorMessage(String errorName){
        return bundle.getString(errorName);
    }

    public static String getWelcomeMessage(){
        return bundle.getString(WELCOME_MESSAGE);
    }
}
