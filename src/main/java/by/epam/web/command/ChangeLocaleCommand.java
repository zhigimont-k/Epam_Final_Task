package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.NoSuchRequestParameterException;
import by.epam.web.util.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeLocaleCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String lang = requestContent.getParameter(JspParameter.LANGUAGE);

            String page = requestContent.getParameter(JspParameter.PAGE);
            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
            router.setPage(page);
            requestContent.setSessionAttribute(JspAttribute.LOCAL, lang);
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        }
        return router;
    }
}
