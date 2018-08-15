package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Review;
import by.epam.web.entity.User;
import by.epam.web.service.*;
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ViewActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();

    /**
     * Retrieves activity's ID from request parameters, looks for activity with this ID
     * in the database, looks for reviews for that activity, sets them as request attributes and
     * forwards to the page with activity information
     *
     * @param requestContent
     * Request and session parameters and attributes
     * @return
     * Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            int activityId = Integer.parseInt(requestContent.getParameter
                    (RequestParameter.ACTIVITY_ID));
            Optional<Activity> found = service.findActivityById(activityId);
            ReviewService reviewService = ServiceFactory.getInstance().getReviewService();
            List<Review> reviewList = reviewService.findReviewByActivityId(activityId);
            UserService userService = ServiceFactory.getInstance().getUserService();
            for (Review review : reviewList) {
                Optional<User> foundUser = userService.findUserById(review.getUserId());
                review.setUserLogin(foundUser.get().getLogin());
            }
            requestContent.setAttribute(RequestParameter.ACTIVITY, found.get());
            requestContent.setAttribute(RequestParameter.REVIEW_LIST, reviewList);
            router.setPage(PageAddress.VIEW_ACTIVITY_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
