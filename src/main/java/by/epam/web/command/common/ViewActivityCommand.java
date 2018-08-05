package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Review;
import by.epam.web.entity.User;
import by.epam.web.service.*;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ViewActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            ActivityService service = ServiceFactory.getInstance().getActivityService();
            int activityId = Integer.parseInt(requestContent.getParameter(RequestParameter.ACTIVITY_ID));
            Optional<Activity> found = service.findActivityById(activityId);
            if (found.isPresent()) {
                ReviewService reviewService = ServiceFactory.getInstance().getReviewService();
                List<Review> reviewList = reviewService.findReviewByActivityId(activityId);
                UserService userService = ServiceFactory.getInstance().getUserService();
                for (Review review : reviewList) {
                    Optional<User> foundUser = userService.findUserById(review.getUserId());
                    if (foundUser.isPresent()) {
                        review.setUserLogin(foundUser.get().getLogin());
                    }
                }
                requestContent.setAttribute(RequestParameter.ACTIVITY, found.get());
                requestContent.setAttribute(RequestParameter.REVIEW_LIST, reviewList);

                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.VIEW_ACTIVITY_PAGE);
            } else {
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
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
