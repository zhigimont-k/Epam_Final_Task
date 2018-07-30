package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;

public interface Command {
    PageRouter execute(SessionRequestContent requestContent);
    default String constructRedirectAddress(SessionRequestContent requestContent) throws NoSuchRequestParameterException{
        String page = requestContent.getParameter(JspParameter.PAGE);
        String query = requestContent.getParameter(JspParameter.QUERY);
        return (query.isEmpty()) ? page : JspAddress.SERVLET_NAME + "?" + query;
    }
}
