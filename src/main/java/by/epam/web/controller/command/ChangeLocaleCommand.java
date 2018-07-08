package by.epam.web.controller.command;

import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeLocaleCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String lang = request.getParameter(JspParameter.LANGUAGE);
        request.getSession().setAttribute(JspAttribute.LOCAL, lang);
        request.getRequestDispatcher("CURRENT_PAGE_NAME").forward(request, response);
    }
}
