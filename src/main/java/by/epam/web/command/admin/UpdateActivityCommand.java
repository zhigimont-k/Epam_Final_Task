package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
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
            int id = Integer.parseInt(requestContent.getParameter
                    (RequestParameter.ACTIVITY_ID));
            String newName = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
            String newDescription = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
            BigDecimal newPrice =
                    new BigDecimal(requestContent.getParameter(RequestParameter.ACTIVITY_PRICE));
            String newStatus = requestContent.getParameter(RequestParameter.ACTIVITY_STATUS);

            Optional<Activity> found = service.findActivityById(id);
            if (found.isPresent()) {
                found.get().setName(newName);
                found.get().setDescription(newDescription);
                found.get().setPrice(newPrice);
                found.get().setStatus(newStatus);
                service.updateActivity(found.get());
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.VIEW_ACTIVITIES);

            } else {
                requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, "Error while updating activity");
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.ERROR_PAGE);
            }
            router.setTransitionType(PageRouter.TransitionType.FORWARD);


        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
