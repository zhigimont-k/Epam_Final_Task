package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            String login = requestContent.getParameter(RequestParameter.LOGIN);
            String password = requestContent.getParameter(RequestParameter.PASSWORD);
            String email = requestContent.getParameter(RequestParameter.EMAIL);
            String userName = requestContent.getParameter(RequestParameter.USER_NAME);
            String phoneNumber = requestContent.getParameter(RequestParameter.PHONE_NUMBER);

            UserService service = ServiceFactory.getInstance().getUserService();

            boolean loginExists = service.loginExists(login);
            boolean emailExists = service.emailExists(email);
            boolean phoneNumberExists = service.phoneNumberExists(phoneNumber);
            if (loginExists) {
                logger.log(Level.INFO, "Login " + login + " exists");
                requestContent.setAttribute(RequestParameter.LOGIN_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.REGISTER_PAGE);
            }
            if (emailExists) {
                logger.log(Level.INFO, "Email " + email + " exists");
                requestContent.setAttribute(RequestParameter.EMAIL_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.REGISTER_PAGE);
            }
            if (phoneNumberExists) {
                logger.log(Level.INFO, "Phone number " + phoneNumber + " exists");
                requestContent.setAttribute(RequestParameter.PHONE_NUMBER_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.REGISTER_PAGE);
            }
            if (!loginExists && !emailExists && !phoneNumberExists) {
                User user = service.registerUser(login, password, email, phoneNumber, userName);
                requestContent.setSessionAttribute(RequestParameter.USER, user);
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.HOME_PAGE);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
