package by.epam.web.controller.listener;

import by.epam.web.controller.constant.JspParameter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class SessionInvalidator implements HttpSessionAttributeListener {
    private static final Logger logger = LogManager.getLogger();
    private boolean loggedIn = false;

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        if (httpSessionBindingEvent.getSession().getAttribute(JspParameter.USER) != null){
            logger.log(Level.INFO, "Logged in");
            loggedIn = true;
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        if (httpSessionBindingEvent.getSession().getAttribute(JspParameter.USER) == null
                && loggedIn) {
            HttpSession session = httpSessionBindingEvent.getSession();
            session.invalidate();
            logger.log(Level.INFO, "Destroyed session");
            loggedIn = false;
        }
    }
}
