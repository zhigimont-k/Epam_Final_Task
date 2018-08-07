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
import by.epam.web.util.request.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import by.epam.web.validation.ReviewValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UpdateReviewCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ReviewService service = ServiceFactory.getInstance().getReviewService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);

            String id = requestContent.getParameter(RequestParameter.REVIEW_ID);
            if (NumberValidator.getInstance().validateId(id)) {
                String newMark = requestContent.getParameter(RequestParameter.REVIEW_MARK);
                String newMessage = requestContent.getParameter(RequestParameter.REVIEW_MESSAGE).trim();
                if (validateReview(newMark, newMessage)) {
                    Optional<Review> found = service.findReviewById(Integer.parseInt(id));
                    if (found.isPresent()) {
                        if (user.getId() == found.get().getUserId()) {
                            service.updateReview(Integer.parseInt(id), Integer.parseInt(newMark),
                                    newMessage);
                            int activityId = found.get().getActivityId();
                            requestContent.removeSessionAttribute(RequestParameter.REVIEW);
                            router.setRedirect(true);
                            router.setPage(PageAddress.VIEW_ACTIVITY + activityId);
                        } else {
                            router.setRedirect(false);
                            router.setPage(PageAddress.FORBIDDEN_ERROR_PAGE);
                        }
                    } else {
                        router.setRedirect(false);
                        router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                    }
                } else {
                    router.setRedirect(true);
                    router.setPage(PageAddress.EDIT_REVIEW_PAGE);
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

    private boolean validateReview(String mark, String message) {
        return ReviewValidator.getInstance().validateMark(mark) &&
                ReviewValidator.getInstance().validateMessage(message);
    }
}
