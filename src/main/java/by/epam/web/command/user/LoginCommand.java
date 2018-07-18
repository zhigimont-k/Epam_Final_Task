package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.command.CommandException;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.JspAddress;
import by.epam.web.constant.JspAttribute;
import by.epam.web.constant.JspParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.UserService;
import by.epam.web.util.NoSuchRequestParameterException;
import by.epam.web.util.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) throws CommandException {
        PageRouter router = new PageRouter();
        try {
            String login = requestContent.getParameter(JspParameter.LOGIN);
            String password = requestContent.getParameter(JspParameter.PASSWORD);
            UserService service = new UserService();
            if (service.findUserByLoginAndPassword(login, password)) {
                User user = service.userLogin(login, password);
                requestContent.setSessionAttribute(JspAttribute.USER, user);
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(JspAddress.HOME_PAGE);
            } else {
                requestContent.setAttribute(JspAttribute.AUTH_FAIL, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.LOGIN_PAGE);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
