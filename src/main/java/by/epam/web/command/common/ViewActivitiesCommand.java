package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ViewActivitiesCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            ActivityService service = ServiceFactory.getInstance().getActivityService();
            List<Activity> activityList = service.findAllActivities();

            requestContent.setAttribute(RequestParameter.ACTIVITY_LIST, activityList);

            router.setRedirect(false);
            router.setPage(PageAddress.ACTIVITIES_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setRedirect(false);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
