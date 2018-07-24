package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
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
            logger.log(Level.INFO, "Redirect address: " +
                    constructRedirectAddress(requestContent));
            requestContent.setSessionAttribute(JspParameter.LOCAL, lang);
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        }
        return router;
    }
}
