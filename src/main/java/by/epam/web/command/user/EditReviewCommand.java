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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class EditReviewCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ReviewService service = ServiceFactory.getInstance().getReviewService();

    /**
     * Retrieves edited review's ID, looks for it in the database, sets it as session attribute and
     * redirects to the edit review page
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
            String reviewId = requestContent.getParameter(RequestParameter.REVIEW_ID);
            Optional<Review> found = service.findReviewById(reviewId);
            if (found.isPresent()) {
                requestContent.setSessionAttribute(RequestParameter.REVIEW, found.get());
                router.setRedirect(true);
                router.setPage(PageAddress.EDIT_REVIEW_PAGE);
            } else {
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
