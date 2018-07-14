package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        PageRouter router = new PageRouter();

        String login = request.getParameter(JspParameter.LOGIN);
        String password = request.getParameter(JspParameter.PASSWORD);
        String email = request.getParameter(JspParameter.EMAIL);
        String userName = request.getParameter(JspParameter.USER_NAME);
        String phoneNumber = request.getParameter(JspParameter.PHONE_NUMBER);

        try {
            UserService service = new UserService();

            boolean loginExists = service.loginExists(login);
            boolean emailExists = service.emailExists(email);
            boolean phoneNumberExists = service.phoneNumberExists(phoneNumber);
            if (loginExists) {
                logger.log(Level.INFO, "Login " + login + " exists");
                request.setAttribute(JspAttribute.LOGIN_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.REGISTER_PAGE);
            }
            if (emailExists) {
                logger.log(Level.INFO, "Email " + email + " exists");
                request.setAttribute(JspAttribute.EMAIL_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.REGISTER_PAGE);
            }
            if (phoneNumberExists) {
                logger.log(Level.INFO, "Phone number " + phoneNumber + " exists");
                request.setAttribute(JspAttribute.PHONE_NUMBER_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.REGISTER_PAGE);
            }
            if (!loginExists && !emailExists && !phoneNumberExists) {
                User user = service.registerUser(login, password, email, phoneNumber, userName);
                session.setAttribute(JspAttribute.USER, user);
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(JspAddress.HOME_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            request.setAttribute(JspAttribute.ERROR_MESSAGE, e.getMessage());

            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.ERROR_PAGE);
        }
        return router;
    }
}
