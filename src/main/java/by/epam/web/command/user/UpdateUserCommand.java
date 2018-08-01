package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            UserService service = ServiceFactory.getInstance().getUserService();
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            String newName = requestContent.getParameter(RequestParameter.USER_NAME);
            String oldPassword = requestContent.getParameter(RequestParameter.PASSWORD);
            String newPassword = requestContent.getParameter(RequestParameter.NEW_PASSWORD);
            if (!oldPassword.isEmpty() && !newPassword.isEmpty()){
                if (service.findUserByLoginAndPassword(user.getLogin(), oldPassword).isPresent()){
                    user = service.updateUser(user.getId(), newPassword, newName).get();
                    logger.log(Level.INFO, "Updated user: "+user.getLogin()+" "+newPassword);
                } else {
                    requestContent.setAttribute(RequestParameter.AUTH_FAIL, true);
                    router.setTransitionType(PageRouter.TransitionType.FORWARD);
                    router.setPage(PageAddress.ACCOUNT_PAGE);
                }
            } else {
                user = service.updateUserName(user.getId(), newName).get();
            }

            requestContent.setSessionAttribute(RequestParameter.USER, user);

            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
            router.setPage(PageAddress.ACCOUNT_PAGE);
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
