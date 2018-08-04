package by.epam.web.controller.filter;

import by.epam.web.command.CommandType;
import by.epam.web.constant.RequestParameter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionInvalidationFilter implements Filter{
    private static Logger logger = LogManager.getLogger();
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String command = servletRequest.getParameter(RequestParameter.COMMAND);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (CommandType.LOGOUT.getName().equalsIgnoreCase(command) &&
                request.getSession().getAttribute(RequestParameter.USER) != null) {
            request.getSession().removeAttribute(RequestParameter.USER);
            request.getSession().invalidate();
            logger.log(Level.INFO, "Destroyed session");
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}