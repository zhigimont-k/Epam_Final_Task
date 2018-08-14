package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.controller.SessionRequestContent;

public class ChangeLocaleCommand implements Command {

    /**
     * Retrieves desired language from request parameters, sets it as session attribute and
     * redirects to the same page
     *
     * @param requestContent
     * Request and session parameters and attributes
     * @return
     * Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        String lang = requestContent.getParameter(RequestParameter.LANGUAGE);
        router.setRedirect(true);
        router.setPage(constructRedirectAddress(requestContent));
        requestContent.setSessionAttribute(RequestParameter.LOCAL, lang);
        return router;
    }

    /**
     * Constructs redirect address using request parameters
     *
     * @param requestContent
     * Request and session parameters and attributes
     * @return
     * Address of the page
     */
    private String constructRedirectAddress(SessionRequestContent requestContent) {
        String page = requestContent.getParameter(RequestParameter.PAGE);
        String query = requestContent.getParameter(RequestParameter.QUERY);
        return (query.isEmpty()) ? page : PageAddress.SERVLET_NAME + "?" + query;
    }
}
