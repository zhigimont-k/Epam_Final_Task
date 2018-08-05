package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class AddActivityCommand implements Command{
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent){
        PageRouter router = new PageRouter();
        try {

            String name = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
            String description = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
            BigDecimal price = BigDecimal.valueOf(Double.valueOf(
                    requestContent.getParameter(RequestParameter.ACTIVITY_PRICE)));
            logger.log(Level.INFO, name+", "+description+", "+price);

            ActivityService service = ServiceFactory.getInstance().getActivityService();

            boolean nameExists = service.nameExists(name);
            if (nameExists) {
                logger.log(Level.INFO, "Activity " + name + " exists");
                requestContent.setAttribute(RequestParameter.ACTIVITY_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.REGISTER_PAGE);
            } else {
                service.addActivity(name, description, price);
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.VIEW_ACTIVITIES);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
