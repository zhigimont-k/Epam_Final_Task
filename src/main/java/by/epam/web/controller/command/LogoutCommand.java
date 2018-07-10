package by.epam.web.controller.command;

import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        session.removeAttribute(JspAttribute.USER);
        response.sendRedirect(JspAddress.HOME_PAGE);
    }
}
