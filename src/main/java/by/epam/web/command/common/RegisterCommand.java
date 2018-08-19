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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class RegisterCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * Retrieves user parameters from request, checks if login, email,
     * phone number and card number are unique
     * If they are, adds new user to database, if they aren't, shows appropriate error messages
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
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
            if (checkUniqueness(requestContent, login, email, phoneNumber, cardNumber)) {
                Optional<User> user = service.registerUser(login, password, email, phoneNumber, userName,
                        cardNumber);
                if (user.isPresent()) {
                    requestContent.setSessionAttribute(RequestParameter.USER, user.get());
                } else {
                    logger.log(Level.ERROR, "Couldn't register user");
                }
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

    /**
     * Checks if given parameters are not already present in the database, if they are,
     * sets error messages in request parameters
     *
     * @param requestContent Request and session parameters and attributes
     * @param login          Login of user
     * @param email          Email of user
     * @param phoneNumber    Phone number of user
     * @param cardNumber     Card number of user
     *
     * @return Result of the check
     *
     * @throws ServiceException if exception occurs while accessing database
     */
    private boolean checkUniqueness(SessionRequestContent requestContent, String login,
                                    String email, String phoneNumber, String cardNumber)
            throws ServiceException {
        boolean flag = true;
        if (service.loginExists(login)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.LOGIN_EXISTS, true);
        }
        if (service.emailExists(email)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.EMAIL_EXISTS, true);
        }
        if (service.phoneNumberExists(phoneNumber)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.PHONE_NUMBER_EXISTS, true);
        }
        if (service.cardNumberExists(cardNumber)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.CARD_NUMBER_EXISTS, true);
        }
        return flag;
    }
}
