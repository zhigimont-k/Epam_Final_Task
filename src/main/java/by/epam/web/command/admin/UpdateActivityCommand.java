package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.Activity;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Optional;

public class UpdateActivityCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {

        PageRouter router = new PageRouter();
        try {

            ActivityService service = ServiceFactory.getInstance().getActivityService();
            int id = Integer.parseInt(requestContent.getParameter("activityId"));
            String newName = requestContent.getParameter("newActivityName");
            String newDescription = requestContent.getParameter("newActivityDescription");
            BigDecimal newPrice =
                    new BigDecimal(requestContent.getParameter("newActivityPrice"));

            Optional<Activity> found = service.findActivityById(id);
            if (found.isPresent()) {
                found.get().setName(newName);
                found.get().setDescription(newDescription);
                found.get().setPrice(newPrice);
                service.updateActivity(found.get());
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
