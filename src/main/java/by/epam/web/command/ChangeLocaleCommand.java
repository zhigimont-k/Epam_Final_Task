package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeLocaleCommand implements Command {
    @Override
    public PageRouter execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String lang = request.getParameter(JspParameter.LANGUAGE);
        PageRouter router = new PageRouter();
        router.setTransitionType(PageRouter.TransitionType.FORWARD);
        router.setPage("CURRENT_PAGE_NAME");
        request.getSession().setAttribute(JspAttribute.LOCAL, lang);
        return router;
    }
}
