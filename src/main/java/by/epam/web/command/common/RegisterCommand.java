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
import by.epam.web.validation.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            String login = requestContent.getParameter(RequestParameter.LOGIN);
            String password = requestContent.getParameter(RequestParameter.PASSWORD);
            String email = requestContent.getParameter(RequestParameter.EMAIL);
            String userName = requestContent.getParameter(RequestParameter.USER_NAME);
            String phoneNumber = requestContent.getParameter(RequestParameter.PHONE_NUMBER);
            String cardNumber = requestContent.getParameter(RequestParameter.CARD_NUMBER);

            if (checkUniqueness(requestContent, login, email, phoneNumber, cardNumber)
                    || validateUser(requestContent, login, password, email,
                    phoneNumber, userName, cardNumber)){
                User user = service.registerUser(login, password, email, phoneNumber, userName,
                        cardNumber);
                requestContent.setSessionAttribute(RequestParameter.USER, user);
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.HOME_PAGE);
            } else {
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.REGISTER_PAGE);
            }

        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }

    private boolean checkUniqueness(SessionRequestContent requestContent,
                                 String login, String email, String phoneNumber, String cardNumber)
            throws ServiceException{
        boolean flag = true;
        boolean loginExists = service.loginExists(login);
        boolean emailExists = service.emailExists(email);
        boolean phoneNumberExists = service.phoneNumberExists(phoneNumber);
        boolean cardNumberExists = service.cardNumberExists(cardNumber);

        if (loginExists) {
            flag = false;
            requestContent.setAttribute(RequestParameter.LOGIN_EXISTS, true);
        }
        if (emailExists) {
            flag = false;
            requestContent.setAttribute(RequestParameter.EMAIL_EXISTS, true);
        }
        if (phoneNumberExists) {
            flag = false;
            requestContent.setAttribute(RequestParameter.PHONE_NUMBER_EXISTS, true);
        }
        if (cardNumberExists) {
            flag = false;
            requestContent.setAttribute(RequestParameter.CARD_NUMBER_EXISTS, true);
        }
        return flag;
    }

    private boolean validateUser(SessionRequestContent requestContent,
                                     String login, String password, String email,
                                 String phoneNumber, String userName, String cardNumber) {
        boolean flag = true;
        if (!UserValidator.getInstance().validateLogin(login)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_LOGIN, true);
        }
        if (!UserValidator.getInstance().validatePassword(password)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_PASSWORD, true);
        }
        if (!UserValidator.getInstance().validateEmail(email)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_EMAIL, true);
        }
        if (!UserValidator.getInstance().validatePhoneNumber(phoneNumber)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_PHONE_NUMBER, true);
        }
        if (!UserValidator.getInstance().validateUserName(userName)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_USER_NAME, true);
        }
        if (!UserValidator.getInstance().validateCardNumber(cardNumber)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_CARD_NUMBER, true);
        }
        return flag;
    }
}
