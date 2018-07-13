package by.epam.web.controller.command;

import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements Command {
    @Override
    //return page address, redirect/forward в сервлете
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();

        String login = request.getParameter(JspParameter.LOGIN);
        String password = request.getParameter(JspParameter.PASSWORD);

        try {
            UserService service = new UserService();
            if (service.passwordMatches(login, password)) {
                User user = service.userLogin(login, password);
                session.setAttribute(JspAttribute.USER, user);
                response.sendRedirect(JspAddress.HOME_PAGE);
            } else {
                request.setAttribute(JspAttribute.AUTH_FAIL, true);
                request.getRequestDispatcher(JspAddress.LOGIN_PAGE).forward(request, response);
            }

        } catch (ServiceException e) {
            throw new ServletException(e); //errorpage
        }
    }
}
