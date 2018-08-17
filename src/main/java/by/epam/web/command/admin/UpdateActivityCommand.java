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

    /**
     * Retrieves activity's ID and new properties from request parameters, checks if activity with
     * the same name but different ID already exists
     * If it does not, updates activity, if it does, shows an error message
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
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
                if (service.updateActivity(activityId, newName, newDescription,
                        newPrice, newStatus)) {
                    requestContent.setSessionAttribute(RequestParameter.DATA_EXISTS, false);
                    requestContent.setSessionAttribute(RequestParameter.ACTIVITY, null);
                    router.setRedirect(true);
                    router.setPage(PageAddress.VIEW_ACTIVITIES);
                } else {
                    logger.log(Level.ERROR, "Couldn't update activity");
                }
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
