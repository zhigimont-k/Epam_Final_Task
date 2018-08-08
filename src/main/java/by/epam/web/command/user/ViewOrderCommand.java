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
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.OrderValidator;
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
            String date = requestContent.getParameter(RequestParameter.ORDER_DATE);
            String time = requestContent.getParameter(RequestParameter.ORDER_TIME);
            String[] activityIdList = requestContent.getParameters(RequestParameter.ACTIVITY_ID);
            if (validateOrder(date, time, activityIdList)) {

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
                order.setDateTime(buildTimestamp(date, time));
                order.setPrice(orderPrice);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }
                requestContent.setSessionAttribute(RequestParameter.ORDER, order);
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_ORDER_PAGE);
            } else {
                router.setRedirect(true);
                router.setPage(PageAddress.ADD_ORDER);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }

    private boolean validateOrder(String date, String time,
                                  String[] activityList) {
        return OrderValidator.getInstance().validateDate(date) &&
                OrderValidator.getInstance().validateTime(time, date) &&
                activityList.length != 0;
    }

    private Timestamp buildTimestamp(String date, String time) {
        String orderTime = date + " " + time;
        if (StringUtils.countMatches(orderTime, ":") == 1) {
            orderTime += ":00";
        }
        return Timestamp.valueOf(orderTime.replace("T", " "));
    }
}
