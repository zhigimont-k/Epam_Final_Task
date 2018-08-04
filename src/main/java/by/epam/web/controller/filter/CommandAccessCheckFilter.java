package by.epam.web.controller.filter;

import by.epam.web.command.CommandFactory;
import by.epam.web.command.CommandAccessLevel;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebFilter(filterName = "CommandAccessCheckFilter",
        urlPatterns = {"/app"})
public class CommandAccessCheckFilter implements Filter {
    private static Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String command = servletRequest.getParameter(RequestParameter.COMMAND);
        if (command == null) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(RequestParameter.USER);
        Optional<CommandAccessLevel> commandRightOptional = CommandFactory.getInstance()
                .getCommandRight(command);
        if (commandRightOptional.isPresent()) {
            CommandAccessLevel commandAccessLevel = commandRightOptional.get();
            if (accessGranted(commandAccessLevel, user)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                logger.log(Level.ERROR, "Tried to call command " + command + " without access");
                logger.log(Level.INFO, "User: "+user+", command right: "+ commandAccessLevel);
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            logger.log(Level.ERROR, "No such command: " + command);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private boolean accessGranted(CommandAccessLevel commandAccessLevel, User user) {
        if (user == null || user.getUserStatus() == User.Status.BANNED) {
            return commandAccessLevel == CommandAccessLevel.GUEST;
        } else {
            User.Status userStatus = user.getUserStatus();
            return userStatus == User.Status.USER && (commandAccessLevel == CommandAccessLevel.USER
                    || commandAccessLevel == CommandAccessLevel.GUEST) ||
                    userStatus == User.Status.ADMIN;
        }
    }

    @Override
    public void destroy() {

    }
}
