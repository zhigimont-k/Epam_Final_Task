package by.epam.web.command.user;

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

import java.util.Optional;

public class UpdateUserCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            String userName = requestContent.getParameter(RequestParameter.USER_NAME);
            String password = requestContent.getParameter(RequestParameter.PASSWORD);
            if (validateParameters(password, userName)){
                String newPassword = requestContent.getParameter(RequestParameter.NEW_PASSWORD);
                logger.log(Level.INFO, userName+" "+password+" "+newPassword);
                if (newPassword.isEmpty()){
                    service.updateUserName(user.getId(), userName);
                    Optional<User> found = service.findUserById(user.getId());
                    if (found.isPresent()){
                        requestContent.setSessionAttribute(RequestParameter.USER, found.get());
                    }
                } else {
                    if (UserValidator.getInstance().validatePassword(newPassword)){
                        if (service.findUserByLoginAndPassword(user.getLogin(), password).isPresent()){
                            service.updateUser(user.getId(), newPassword, userName);
                            Optional<User> found = service.findUserById(user.getId());
                            if (found.isPresent()){
                                requestContent.setSessionAttribute(RequestParameter.USER, found.get());
                            }
                        } else {
                            requestContent.setAttribute(RequestParameter.AUTH_FAIL, true);
                            router.setTransitionType(PageRouter.TransitionType.FORWARD);
                            router.setPage(PageAddress.VIEW_USER_INFO);
                        }
                    } else {
                        requestContent.setAttribute(RequestParameter.ILLEGAL_INPUT, true);

                        router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                        router.setPage(PageAddress.VIEW_USER_INFO);
                    }
                }
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.VIEW_USER_INFO);
            } else {
                requestContent.setAttribute(RequestParameter.ILLEGAL_INPUT, true);

                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.VIEW_USER_INFO);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }

    private boolean validateParameters(String password,
                                       String userName){
        return UserValidator.getInstance().validatePassword(password) &&
                UserValidator.getInstance().validateUserName(userName);
    }
}
