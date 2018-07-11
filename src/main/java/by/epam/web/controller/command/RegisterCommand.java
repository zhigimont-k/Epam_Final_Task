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

public class RegisterCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();

        String login = request.getParameter(JspParameter.LOGIN);
        String password = request.getParameter(JspParameter.PASSWORD);
        String email = request.getParameter(JspParameter.EMAIL);
        String userName = request.getParameter(JspParameter.USER_NAME);
        String phoneNumber = request.getParameter(JspParameter.PHONE_NUMBER);

        try {
            UserService service = new UserService();
            if (service.loginExists(login)){

            } else if (service.emailExists(email)){

            } else if (service.phoneNumberExists(phoneNumber)){

            } else {
                User user = service.registerUser(login, password, email, phoneNumber, userName);
                session.setAttribute(JspAttribute.USER, user);
                response.sendRedirect(JspAddress.HOME_PAGE);
            }
        } catch (ServiceException e) {
            throw new ServletException(e);
        }

    }
}
