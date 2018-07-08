package by.epam.web.controller.util;

import by.epam.web.controller.constant.ErrorMessageName;
import by.epam.web.controller.constant.JspParameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

public class UserErrorHandler {

    public static void handleError(HttpServletRequest request, HttpServletResponse response, String errorName) throws ServletException, IOException{
        ResourceBundle bundle = ResourceBundle.getBundle("locale.locale");
        String message = bundle.getString(errorName);
        request.setAttribute(JspParameter.ERROR_MESSAGE, message);
        request.getRequestDispatcher("REFRESH_CURRENT_PAGE").forward(request, response);
    }
}
