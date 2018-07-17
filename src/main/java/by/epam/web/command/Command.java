package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.NoSuchRequestParameterException;
import by.epam.web.util.SessionRequestContent;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Command {
    PageRouter execute(SessionRequestContent requestContent) throws IOException, ServletException;
    default String constructRedirectAddress(SessionRequestContent requestContent) throws NoSuchRequestParameterException{
        String page = requestContent.getParameter(JspParameter.PAGE);
        String query = requestContent.getParameter(JspParameter.QUERY);
        String address = (query.isEmpty()) ? page : JspAddress.SERVLET_NAME + "?" + query;
        return address;
    }
}
