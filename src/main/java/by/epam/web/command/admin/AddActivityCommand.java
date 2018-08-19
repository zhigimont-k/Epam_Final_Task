package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();

    /**
     * Retrieves necessary parameters from request, checks if activity
     * with the same name already exists
     * Adds new activity if it does not, shows error message if it does
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String name = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
            String description = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
            String price = requestContent.getParameter(RequestParameter.ACTIVITY_PRICE);
            if (service.nameExists(name)) {
                requestContent.setAttribute(RequestParameter.DATA_EXISTS, true);
                router.setPage(PageAddress.ADD_ACTIVITY_PAGE);
            } else {
                if (!service.addActivity(name, description, price)) {
                    logger.log(Level.ERROR, "Couldn't add activity");
                }
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_ACTIVITIES);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
