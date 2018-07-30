package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import by.epam.web.service.ActivityService;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
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
            int userId = ((User) requestContent.getSessionAttribute(JspParameter.USER)).getId();
            String orderTime = requestContent.getParameter(JspParameter.ORDER_TIME);
            if (StringUtils.countMatches(orderTime, ":") == 1) {
                orderTime += ":00";
            }
            String[] activityIdList = requestContent.getParameters(JspParameter.ACTIVITY_ID);
            List<Activity> activityList = new ArrayList<>();
            ActivityService activityService = ServiceFactory.getInstance().getActivityService();
            for (String activityId : activityIdList) {
                int id = Integer.parseInt(activityId);
                Optional<Activity> found = activityService.findActivityById(id);
                if (found.isPresent()) {
                    activityList.add(found.get());
                }
            }

            OrderService service = ServiceFactory.getInstance().getOrderService();

            service.addOrder(userId, Timestamp.valueOf(
                    orderTime.replace("T", " ")), activityList);
            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
            router.setPage("app?command=viewUserOrders");
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
