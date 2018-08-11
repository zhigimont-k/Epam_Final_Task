package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.controller.SessionRequestContent;
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
            if (checkUniqueness(requestContent, login, email, phoneNumber, cardNumber)){
                requestContent.removeSessionAttribute(RequestParameter.ILLEGAL_INPUT);
                User user = service.registerUser(login, password, email, phoneNumber, userName,
                        cardNumber);
                requestContent.setSessionAttribute(RequestParameter.USER, user);
                router.setRedirect(true);
                router.setPage(PageAddress.HOME_PAGE);
            } else {
                router.setPage(PageAddress.REGISTER_PAGE);
            }

        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }

    private boolean checkUniqueness(SessionRequestContent requestContent, String login,
                                    String email, String phoneNumber, String cardNumber)
            throws ServiceException{
        boolean flag = true;
        if (service.loginExists(login)){
            flag = false;
            requestContent.setAttribute(RequestParameter.LOGIN_EXISTS, true);
        }
        if (service.emailExists(email)){
            flag = false;
            requestContent.setAttribute(RequestParameter.EMAIL_EXISTS, true);
        }
        if (service.phoneNumberExists(phoneNumber)){
            flag = false;
            requestContent.setAttribute(RequestParameter.PHONE_NUMBER_EXISTS, true);
        }
        if (service.cardNumberExists(cardNumber)){
            flag = false;
            requestContent.setAttribute(RequestParameter.CARD_NUMBER_EXISTS, true);
        }

        return flag;
    }
}
