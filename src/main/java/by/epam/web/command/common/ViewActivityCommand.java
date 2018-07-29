package by.epam.web.command.common;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Review;
import by.epam.web.entity.User;
import by.epam.web.service.*;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ViewActivityCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            ActivityService service = ServiceFactory.getInstance().getActivityService();
            int activityId = Integer.parseInt(requestContent.getParameter(JspParameter.ACTIVITY_ID));
            Optional<Activity> found = service.findActivityById(activityId);
            if (found.isPresent()){
                ReviewService reviewService = ServiceFactory.getInstance().getReviewService();
                List<Review> reviewList = reviewService.findReviewByActivityId(activityId);
                UserService userService = ServiceFactory.getInstance().getUserService();
                for (Review review : reviewList){
                    Optional<User> foundUser = userService.findUserById(review.getUserId());
                    if (foundUser.isPresent()){
                        review.setUserLogin(foundUser.get().getLogin());
                    }
                }
                requestContent.setAttribute(JspParameter.ACTIVITY, found.get());
                requestContent.setAttribute(JspParameter.REVIEW_LIST, reviewList);

                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.VIEW_ACTIVITY);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(JspParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.ERROR_PAGE);
        }
        return router;
    }
}
