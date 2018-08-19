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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class EditActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static final ActivityService service = ServiceFactory.getInstance().getActivityService();

    /**
     * Retrieves activity's ID from request parameters, looks for an activity with the same ID
     * in database, sets found activity as session attribute and redirects to
     * an editing page of this activity
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String activityId = requestContent.getParameter(RequestParameter.ACTIVITY_ID);
            Optional<Activity> found = service.findActivityById(Integer.parseInt(activityId));
            if (found.isPresent()) {
                requestContent.setSessionAttribute(RequestParameter.ACTIVITY, found.get());
                router.setRedirect(true);
                router.setPage(PageAddress.EDIT_ACTIVITY_PAGE);
            } else {
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                logger.log(Level.ERROR, "Couldn't find activity");
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
