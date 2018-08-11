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
import by.epam.web.controller.SessionRequestContent;
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
            String id = requestContent.getParameter(RequestParameter.REVIEW_ID);
            String newMark = requestContent.getParameter(RequestParameter.REVIEW_MARK);
            String newMessage = requestContent.getParameter(RequestParameter.REVIEW_MESSAGE).trim();
            Optional<Review> found = service.findReviewById(Integer.parseInt(id));
            service.updateReview(Integer.parseInt(id), Integer.parseInt(newMark),
                    newMessage);
            int activityId = found.get().getActivityId();
            requestContent.setSessionAttribute(RequestParameter.REVIEW, null);
            router.setRedirect(true);
            router.setPage(PageAddress.VIEW_ACTIVITY + activityId);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
