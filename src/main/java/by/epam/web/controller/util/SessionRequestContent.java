package by.epam.web.controller.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SessionRequestContent {
    private static SessionRequestContent instance = new SessionRequestContent();
    private Map<String, Object> requestAttributes;
    private Map<String, String[]> requestParameters;
    private Map<String, Object> sessionAttributes;

    public static SessionRequestContent getInstance(){
        return instance;
    }

    public void extractValues(HttpServletRequest request) {

    }

    public void insertAttributes(HttpServletRequest request){

    }
}
