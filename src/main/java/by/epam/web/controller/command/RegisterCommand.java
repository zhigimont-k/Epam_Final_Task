package by.epam.web.controller.command;

import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.service.ServiceException;
import by.epam.web.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter(JspParameter.LOGIN);
        String password = request.getParameter(JspParameter.PASSWORD);

        try {
            UserService service = new UserService();
            service.registerUser(login, password);
            request.getRequestDispatcher(JspAddress.MAIN).forward(request, response);
        } catch (ServiceException e){
            request.setAttribute(JspParameter.ERROR_MESSAGE, e.getMessage());
            request.getRequestDispatcher(JspAddress.ERROR).forward(request, response);
        }

    }
}
