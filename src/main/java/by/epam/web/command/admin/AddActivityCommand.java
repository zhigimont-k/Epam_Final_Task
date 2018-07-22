package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspAttribute;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.Activity;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.NoSuchRequestParameterException;
import by.epam.web.util.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;

public class AddActivityCommand implements Command{
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            String name = requestContent.getParameter(JspParameter.ACTIVITY_NAME);
            String description = requestContent.getParameter(JspParameter.ACTIVITY_DESCRIPTION);
            BigDecimal price = BigDecimal.valueOf(Double.valueOf(
                    requestContent.getParameter(JspParameter.ACTIVITY_PRICE)));
            logger.log(Level.INFO, name+", "+description+", "+price);

            ActivityService service = ServiceFactory.getInstance().getActivityService();

            boolean nameExists = service.nameExists(name);
            if (nameExists) {
                logger.log(Level.INFO, "Activity  " + name + " exists");
                requestContent.setAttribute(JspAttribute.ACTIVITY_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.REGISTER_PAGE);
            } else {
                service.addActivity(name, description, price);
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(JspAddress.HOME_PAGE);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(JspAttribute.ERROR_MESSAGE, e.getMessage());

            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.ERROR_PAGE);
        }
        return router;
    }
}
