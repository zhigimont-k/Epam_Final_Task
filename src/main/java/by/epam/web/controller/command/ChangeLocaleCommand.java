package by.epam.web.controller.command;

import by.epam.task4.util.JspAddress;
import by.epam.task4.util.JspParameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeLocaleCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String lang = request.getParameter(JspParameter.LANGUAGE);
        request.getSession().setAttribute(JspParameter.LOCAL, lang);
        try {
            request.getRequestDispatcher(JspAddress.MAIN).forward(request, response);
        } catch (IOException e){
            request.setAttribute(JspParameter.ERROR_MESSAGE, e.getMessage());
            request.getRequestDispatcher(JspAddress.ERROR).forward(request, response);
        }

    }
}
