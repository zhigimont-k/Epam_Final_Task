package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
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

public class ViewOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            int userId = ((User) requestContent.getSessionAttribute(RequestParameter.USER)).getId();
            String orderTime = requestContent.getParameter(RequestParameter.ORDER_TIME);
            if (StringUtils.countMatches(orderTime, ":") == 1) {
                orderTime += ":00";
            }
            String[] activityIdList = requestContent.getParameters(RequestParameter.ACTIVITY_ID);
            BigDecimal orderPrice = new BigDecimal(BigInteger.ZERO);
            List<Activity> activityList = new ArrayList<>();
            ActivityService activityService = ServiceFactory.getInstance().getActivityService();
            for (String activityId : activityIdList) {
                int id = Integer.parseInt(activityId);
                Optional<Activity> found = activityService.findActivityById(id);
                if (found.isPresent()) {
                    activityList.add(found.get());
                    orderPrice = orderPrice.add(found.get().getPrice());
                }
            }
            Order order = new Order();
            order.setUserId(userId);
            order.setDateTime(Timestamp.valueOf(orderTime.replace("T", " ")));
            order.setPrice(orderPrice);
            for (Activity activity : activityList) {
                order.addActivity(activity);
            }
            logger.log(Level.INFO, "Viewing order: " + order);
            requestContent.setSessionAttribute(RequestParameter.ORDER, order);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.VIEW_ORDER_PAGE);

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
