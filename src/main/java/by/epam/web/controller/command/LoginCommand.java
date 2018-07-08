package by.epam.web.controller.command;

import by.epam.web.controller.constant.ErrorMessageName;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.controller.util.UserErrorHandler;
import by.epam.web.dao.user.impl.IncorrectPasswordException;
import by.epam.web.dao.user.impl.NoSuchUserException;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter(JspParameter.LOGIN);
        String password = request.getParameter(JspParameter.PASSWORD);

        try {
            UserService service = new UserService();
            User user = service.userLogin(login, password);
            request.setAttribute(JspAttribute.USER, user);
            service.userLogin(login, password);
            response.sendRedirect(JspAddress.MAIN);
        } catch (NoSuchUserException e) {
            UserErrorHandler.handleError(request, response, ErrorMessageName.NO_SUCH_USER);
        } catch (IncorrectPasswordException e) {
            UserErrorHandler.handleError(request, response, ErrorMessageName.INCORRECT_PASSWORD);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
    }
}
