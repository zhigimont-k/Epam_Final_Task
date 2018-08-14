package by.epam.web.controller.filter;

import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminPageFilter",
        urlPatterns = {"/addService", "/editService"})
public class AdminPageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * Checks if user is admin.
     * If he is, continues filtering, otherwise send a 403 error
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(RequestParameter.USER);
        if (User.Status.ADMIN.getName().equalsIgnoreCase(user.getStatus())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {

    }
}
