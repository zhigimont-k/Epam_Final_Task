package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.controller.SessionRequestContent;

public class ChangeLocaleCommand implements Command {

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        String lang = requestContent.getParameter(RequestParameter.LANGUAGE);

        router.setRedirect(true);
        router.setPage(constructRedirectAddress(requestContent));
        requestContent.setSessionAttribute(RequestParameter.LOCAL, lang);
        return router;
    }

    private String constructRedirectAddress(SessionRequestContent requestContent) {
        String page = requestContent.getParameter(RequestParameter.PAGE);
        String query = requestContent.getParameter(RequestParameter.QUERY);
        return (query.isEmpty()) ? page : PageAddress.SERVLET_NAME + "?" + query;
    }
}
