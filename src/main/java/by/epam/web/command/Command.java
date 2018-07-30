package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Command {
    Logger logger = LogManager.getLogger();
    PageRouter execute(SessionRequestContent requestContent);
    default String constructRedirectAddress(SessionRequestContent requestContent) throws NoSuchRequestParameterException{
        String page = requestContent.getParameter(JspParameter.PAGE);
        String query = requestContent.getParameter(JspParameter.QUERY);
        logger.log(Level.INFO, "Page: "+page+", query: "+query);
        return (query.isEmpty()) ? page : JspAddress.SERVLET_NAME + "?" + query;
    }
}
