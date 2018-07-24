package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String login = requestContent.getParameter(JspParameter.LOGIN);
            String password = requestContent.getParameter(JspParameter.PASSWORD);
            UserService service = ServiceFactory.getInstance().getUserService();
            if (service.findUserByLoginAndPassword(login, password)) {
                User user = service.userLogin(login, password).get();
                requestContent.setSessionAttribute(JspParameter.USER, user);
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(JspAddress.HOME_PAGE);
            } else {
                requestContent.setAttribute(JspParameter.AUTH_FAIL, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.LOGIN_PAGE);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.ERROR_PAGE);
        }
        return router;
    }
}
