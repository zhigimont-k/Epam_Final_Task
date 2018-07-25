package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.Review;
import by.epam.web.service.ReviewService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UpdateReviewCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            ReviewService service = ServiceFactory.getInstance().getReviewService();
            int id = Integer.parseInt(requestContent.getParameter
                    (JspParameter.REVIEW_ID));
            int newMark = Integer.parseInt(requestContent.getParameter
                    (JspParameter.REVIEW_MARK));
            String newMessage = requestContent.getParameter(JspParameter.REVIEW_MESSAGE);

            Optional<Review> found = service.findReviewById(id);
            if (found.isPresent()) {
                service.updateReview(id, newMark, newMessage);
                requestContent.setAttribute(JspParameter.OPERATION_RESULT, true);

            } else {
                requestContent.setAttribute(JspParameter.OPERATION_RESULT, false);
            }
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.OPERATION_RESULT);


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
