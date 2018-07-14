package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command {

    @Override
    public PageRouter execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        HttpSession session = request.getSession();
        PageRouter router = new PageRouter();
        session.removeAttribute(JspAttribute.USER);

        router.setTransitionType(PageRouter.TransitionType.REDIRECT);
        router.setPage(JspAddress.HOME_PAGE);
        return router;
    }
}
