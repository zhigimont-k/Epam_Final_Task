package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.controller.SessionRequestContent;

public class ChangeLocaleCommand implements Command {
    private static final String RUSSIAN_LANGUAGE = "ru";
    private static final String ENGLISH_LANGUAGE = "en";

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        String lang = requestContent.getParameter(RequestParameter.LANGUAGE);
        if (validateLanguage(lang)){
            router.setRedirect(true);
            router.setPage(constructRedirectAddress(requestContent));
            requestContent.setSessionAttribute(RequestParameter.LOCAL, lang);
        } else {
            router.setPage(PageAddress.BAD_REQUEST_ERROR_PAGE);
        }
        return router;
    }

    private String constructRedirectAddress(SessionRequestContent requestContent) {
        String page = requestContent.getParameter(RequestParameter.PAGE);
        String query = requestContent.getParameter(RequestParameter.QUERY);
        return (query.isEmpty()) ? page : PageAddress.SERVLET_NAME + "?" + query;
    }

    private boolean validateLanguage(String language){
        return RUSSIAN_LANGUAGE.equalsIgnoreCase(language) ||
                ENGLISH_LANGUAGE.equalsIgnoreCase(language);
    }
}
