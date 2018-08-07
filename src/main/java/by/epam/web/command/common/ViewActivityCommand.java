package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Review;
import by.epam.web.entity.User;
import by.epam.web.service.*;
import by.epam.web.util.request.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ViewActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String id = requestContent.getParameter(RequestParameter.ACTIVITY_ID);
            if (NumberValidator.getInstance().validateId(id)){
                int activityId = Integer.parseInt(id);
                Optional<Activity> found = service.findActivityById(activityId);
                if (found.isPresent()) {
                    ReviewService reviewService = ServiceFactory.getInstance().getReviewService();
                    List<Review> reviewList = reviewService.findReviewByActivityId(activityId);
                    UserService userService = ServiceFactory.getInstance().getUserService();
                    for (Review review : reviewList) {
                        Optional<User> foundUser = userService.findUserById(review.getUserId());
                        if (foundUser.isPresent()) {
                            review.setUserLogin(foundUser.get().getLogin());
                        } else {
                            logger.log(Level.ERROR, "Couldn't find user to get login for review");
                        }
                    }
                    requestContent.setAttribute(RequestParameter.ACTIVITY, found.get());
                    requestContent.setAttribute(RequestParameter.REVIEW_LIST, reviewList);

                    router.setRedirect(false);
                    router.setPage(PageAddress.VIEW_ACTIVITY_PAGE);
                } else {
                    router.setRedirect(false);
                    router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                }
            } else {
                router.setRedirect(false);
                router.setPage(PageAddress.BAD_REQUEST_ERROR_PAGE);
            }

        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setRedirect(false);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
