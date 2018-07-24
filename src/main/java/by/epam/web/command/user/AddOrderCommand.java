package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.User;
import by.epam.web.service.ActivityService;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            int userId = ((User)requestContent.getSessionAttribute(JspParameter.USER)).getId();

            Timestamp time = new Timestamp(1L);

            List<Activity> activityList = new ArrayList<>();
            ActivityService activityService = ServiceFactory.getInstance().getActivityService();
            String[] activityNames = requestContent.getParameters(JspParameter.ACTIVITY_NAME);
            for (String activityName : activityNames){
                Optional<Activity> found = activityService.findActivityByName(activityName);
                if (found.isPresent()){
                    activityList.add(found.get());
                }
            }

            OrderService service = ServiceFactory.getInstance().getOrderService();

            service.addOrder(userId, time, activityList);
            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
            router.setPage(JspAddress.HOME_PAGE);
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
