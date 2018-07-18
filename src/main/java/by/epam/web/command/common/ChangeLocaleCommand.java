package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.constant.JspAddress;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.JspAttribute;
import by.epam.web.constant.JspParameter;
import by.epam.web.util.NoSuchRequestParameterException;
import by.epam.web.util.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeLocaleCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String lang = requestContent.getParameter(JspParameter.LANGUAGE);

            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
            router.setPage(constructRedirectAddress(requestContent));
            //получать адрес через фильтр
            requestContent.setSessionAttribute(JspAttribute.LOCAL, lang);
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        }
        return router;
    }

    private String constructRedirectAddress(SessionRequestContent requestContent) throws NoSuchRequestParameterException{
        String page = requestContent.getParameter(JspParameter.PAGE);
        String query = requestContent.getParameter(JspParameter.QUERY);
        return (query.isEmpty()) ? page : JspAddress.SERVLET_NAME + "?" + query;
    }
}
