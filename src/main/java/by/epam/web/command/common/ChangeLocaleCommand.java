package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeLocaleCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String lang = requestContent.getParameter(RequestParameter.LANGUAGE);

            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
            router.setPage(constructRedirectAddress(requestContent));
            requestContent.setSessionAttribute(RequestParameter.LOCAL, lang);
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
        }
        return router;
    }

    private String constructRedirectAddress(SessionRequestContent requestContent)
            throws NoSuchRequestParameterException{
        String page = requestContent.getParameter(RequestParameter.PAGE);
        String query = requestContent.getParameter(RequestParameter.QUERY);
        return (query.isEmpty()) ? page : PageAddress.SERVLET_NAME + "?" + query;
    }
}
