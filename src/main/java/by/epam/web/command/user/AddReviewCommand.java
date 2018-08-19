package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ReviewService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddReviewCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ReviewService service = ServiceFactory.getInstance().getReviewService();

    /**
     * Retrieves user's ID and review properties from session and request parameters
     * and adds a new review
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            int userId = ((User) requestContent.getSessionAttribute(RequestParameter.USER)).getId();
            String activityId = requestContent.getParameter(RequestParameter.ACTIVITY_ID);
            String mark = requestContent.getParameter(RequestParameter.REVIEW_MARK);
            String message = requestContent.getParameter(RequestParameter.REVIEW_MESSAGE).trim();
            if (!service.addReview(userId, Integer.parseInt(activityId), mark, message)) {
                logger.log(Level.ERROR, "Couldn't add review");
            }
            router.setRedirect(true);
            router.setPage(PageAddress.VIEW_ACTIVITY + activityId);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
