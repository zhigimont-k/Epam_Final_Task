package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public PageRouter execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        PageRouter router = new PageRouter();

        String login = request.getParameter(JspParameter.LOGIN);
        String password = request.getParameter(JspParameter.PASSWORD);

        try {
            UserService service = new UserService();
            if (service.findUserByLoginAndPassword(login, password)) {
                User user = service.userLogin(login, password);
                session.setAttribute(JspAttribute.USER, user);

                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(JspAddress.HOME_PAGE);
                return router;
            } else {
                request.setAttribute(JspAttribute.AUTH_FAIL, true);

                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.LOGIN_PAGE);
            }

        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);

            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.ERROR_PAGE);
        }
        return router;
    }
}
