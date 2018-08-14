package by.epam.web.command.user;

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

import java.util.Optional;

public class UpdateUserCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * Retrieves user, new username, old password and new password from request and session
     * If user with the same login and old password exists in the database,
     * updates only username if new password isn't set or both password and username if it is
     * If no user is found, shows error message
     *
     * @param requestContent
     * Request and session parameters and attributes
     * @return
     * Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            String userName = requestContent.getParameter(RequestParameter.USER_NAME);
            String password = requestContent.getParameter(RequestParameter.PASSWORD);
            String newPassword = requestContent.getParameter(RequestParameter.NEW_PASSWORD);
            if (service.findUserByLoginAndPassword(user.getLogin(), password).isPresent()) {
                requestContent.setSessionAttribute(RequestParameter.AUTH_FAIL, false);
                if (newPassword.isEmpty()) {
                    service.updateUserName(String.valueOf(user.getId()), userName);
                    Optional<User> found = service.findUserById(String.valueOf(user.getId()));
                    requestContent.setSessionAttribute(RequestParameter.USER, found.get());
                } else {
                    service.updateUser(String.valueOf(user.getId()), newPassword, userName);
                    Optional<User> found = service.findUserById(String.valueOf(user.getId()));
                    requestContent.setSessionAttribute(RequestParameter.USER, found.get());
                }
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_USER_INFO);
            } else {
                requestContent.setSessionAttribute(RequestParameter.AUTH_FAIL, true);
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_USER_INFO);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
