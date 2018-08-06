package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.User;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ReviewService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import by.epam.web.validation.ReviewValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AddReviewCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ReviewService service = ServiceFactory.getInstance().getReviewService();
    private static ActivityService activityService = ServiceFactory.getInstance().getActivityService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            int userId = ((User) requestContent.getSessionAttribute(RequestParameter.USER)).getId();
            String activityId = requestContent.getParameter(RequestParameter.ACTIVITY_ID);
            String mark = requestContent.getParameter(RequestParameter.REVIEW_MARK);
            String message = requestContent.getParameter(RequestParameter.REVIEW_MESSAGE).trim();

            if (NumberValidator.getInstance().validateId(activityId)){
                if (validateReview(requestContent, mark, message)){
                    Optional<Activity> found = activityService.findActivityById(
                            Integer.parseInt(activityId));
                    if (found.isPresent()){
                        service.addReview(userId, Integer.parseInt(activityId),
                                Integer.parseInt(mark), message);

                        router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                        router.setPage(PageAddress.VIEW_ACTIVITY + activityId);
                    } else {
                        router.setTransitionType(PageRouter.TransitionType.FORWARD);
                        router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                    }
                } else {
                    router.setTransitionType(PageRouter.TransitionType.FORWARD);
                    router.setPage(PageAddress.VIEW_ACTIVITY + activityId);
                }
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

    private boolean validateReview(SessionRequestContent requestContent, String mark, String message){
        boolean flag = true;
        if (!ReviewValidator.getInstance().validateMark(mark)){
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_REVIEW_MARK, true);
        }
        if (!ReviewValidator.getInstance().validateMessage(message)){
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_REVIEW_MESSAGE, true);
        }
        return flag;
    }
}
