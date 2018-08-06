package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
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

            if (checkUniqueness(login, email, phoneNumber, cardNumber)){
                requestContent.removeSessionAttribute(RequestParameter.DATA_EXISTS);
                if (validateUser(login, password, email,
                        phoneNumber, userName, cardNumber)){
                    requestContent.removeSessionAttribute(RequestParameter.ILLEGAL_INPUT);
                    User user = service.registerUser(login, password, email, phoneNumber, userName,
                            cardNumber);
                    requestContent.setSessionAttribute(RequestParameter.USER, user);
                    //////////
                    router.setTransitionType(PageRouter.TransitionType.FORWARD);
                    //////////
                    router.setPage(PageAddress.HOME_PAGE);
                } else {
                    requestContent.setAttribute(RequestParameter.ILLEGAL_INPUT, true);
                    router.setTransitionType(PageRouter.TransitionType.FORWARD);
                    router.setPage(PageAddress.REGISTER_PAGE);
                }

            } else {
                requestContent.setAttribute(RequestParameter.DATA_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.REGISTER_PAGE);
            }

        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }

    private boolean checkUniqueness(String login, String email, String phoneNumber, String cardNumber)
            throws ServiceException{
        boolean loginExists = service.loginExists(login);
        boolean emailExists = service.emailExists(email);
        boolean phoneNumberExists = service.phoneNumberExists(phoneNumber);
        boolean cardNumberExists = service.cardNumberExists(cardNumber);

        return !loginExists && !emailExists && !phoneNumberExists && !cardNumberExists;
    }

    private boolean validateUser(String login, String password, String email,
                                 String phoneNumber, String userName, String cardNumber) {

        return UserValidator.getInstance().validateLogin(login) &&
                UserValidator.getInstance().validatePassword(password) &&
                UserValidator.getInstance().validateEmail(email) &&
                UserValidator.getInstance().validatePhoneNumber(phoneNumber) &&
                UserValidator.getInstance().validateUserName(userName) &&
                UserValidator.getInstance().validateCardNumber(cardNumber);
    }
}
