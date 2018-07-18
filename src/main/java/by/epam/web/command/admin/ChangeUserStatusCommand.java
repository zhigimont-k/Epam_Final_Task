package by.epam.web.command.admin;

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

import java.util.List;

public class ChangeUserStatusCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) throws CommandException {
        PageRouter router = new PageRouter();
        try {
            UserService service = new UserService();
            String login = requestContent.getParameter(JspParameter.LOGIN);
            String status = requestContent.getParameter(JspParameter.USER_STATUS);
            service.changeUserStatus(login, status);

            List<User> userList = service.findAllUsers();

            requestContent.setAttribute(JspAttribute.USER_LIST, userList);

            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.USERS_PAGE);
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
