package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.ActivityValidator;
import by.epam.web.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Optional;

public class UpdateActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {

        PageRouter router = new PageRouter();
        try {
            int activityId = ((Activity) requestContent.getSessionAttribute
                    (RequestParameter.ACTIVITY)).getId();
            String newName = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
            String newDescription = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
            String newPrice = requestContent.getParameter(RequestParameter.ACTIVITY_PRICE);
            String newStatus = requestContent.getParameter(RequestParameter.ACTIVITY_STATUS);
            boolean activityExists = service.activityExists(activityId, newName);
            if (activityExists) {
                requestContent.setSessionAttribute(RequestParameter.DATA_EXISTS, true);
                router.setRedirect(true);
                router.setPage(PageAddress.EDIT_ACTIVITY_PAGE);
            } else {
                Optional<Activity> found = service.findActivityById(activityId);
                if (found.isPresent()) {
                    found.get().setName(newName);
                    found.get().setDescription(newDescription);
                    found.get().setPrice(new BigDecimal(newPrice));
                    found.get().setStatus(newStatus);
                    service.updateActivity(activityId, newName, newDescription,
                            newPrice, newStatus);
                    requestContent.setSessionAttribute(RequestParameter.DATA_EXISTS, false);
                    requestContent.setSessionAttribute(RequestParameter.ACTIVITY, null);
                    router.setRedirect(true);
                    router.setPage(PageAddress.VIEW_ACTIVITIES);
                } else {
                    router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                }
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
