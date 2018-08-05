package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Review;
import by.epam.web.entity.User;
import by.epam.web.service.ReviewService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UpdateReviewCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);

            ReviewService service = ServiceFactory.getInstance().getReviewService();
            int id = Integer.parseInt(requestContent.getParameter
                    (RequestParameter.REVIEW_ID));
            int newMark = Integer.parseInt(requestContent.getParameter
                    (RequestParameter.REVIEW_MARK));
            String newMessage = requestContent.getParameter(RequestParameter.REVIEW_MESSAGE);

            Optional<Review> found = service.findReviewById(id);
            if (found.isPresent()) {
                if (user.getId() == found.get().getUserId()){
                    service.updateReview(id, newMark, newMessage);
                    int activityId = found.get().getActivityId();
                    router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                    router.setPage(PageAddress.VIEW_ACTIVITY + activityId);
                } else {
                    router.setTransitionType(PageRouter.TransitionType.FORWARD);
                    router.setPage(PageAddress.FORBIDDEN_ERROR_PAGE);
                }

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
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;

    }
}
