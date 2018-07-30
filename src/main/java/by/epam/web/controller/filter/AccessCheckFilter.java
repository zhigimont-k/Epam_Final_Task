package by.epam.web.controller.filter;

import by.epam.web.command.CommandFactory;
import by.epam.web.command.CommandRight;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class AccessCheckFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String command = servletRequest.getParameter(JspParameter.COMMAND);
        if (command == null) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(JspParameter.USER);
        Optional<CommandRight> commandRightOptional = CommandFactory.getInstance()
                .getCommandRight(command);
        if (commandRightOptional.isPresent()) {
            CommandRight commandRight = commandRightOptional.get();
            if (accessGranted(commandRight, user)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                logger.log(Level.ERROR, "Tried to call command " + command + " without access");
                logger.log(Level.INFO, "User: "+user+", command right: "+commandRight);
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            logger.log(Level.ERROR, "No such command: " + command);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private boolean accessGranted(CommandRight commandRight, User user) {
        if (user == null || user.getUserStatus() == User.Status.BANNED) {
            return commandRight == CommandRight.GUEST;
        } else {
            User.Status userStatus = user.getUserStatus();
            return userStatus == User.Status.USER && (commandRight == CommandRight.USER
                    || commandRight == CommandRight.GUEST) ||
                    userStatus == User.Status.ADMIN;
        }
    }

    @Override
    public void destroy() {

    }
}
