package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import by.epam.web.service.ActivityService;
import by.epam.web.service.OrderService;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class ViewOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();
    private static ActivityService activityService =
            ServiceFactory.getInstance().getActivityService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            int userId = ((User) requestContent.getSessionAttribute(RequestParameter.USER)).getId();
            String date = requestContent.getParameter(RequestParameter.ORDER_DATE);
            String time = requestContent.getParameter(RequestParameter.ORDER_TIME);
            String[] activityIdList = requestContent.getParameters(RequestParameter.ACTIVITY_ID);
            BigDecimal orderPrice = new BigDecimal(BigInteger.ZERO);
            List<Activity> activityList = new ArrayList<>();
//            logger.log(Level.INFO, "order exists: "+service.orderExists
//                    (userId, Timestamp.valueOf(getUTCTimestamp(date, time))));
            if (service.orderExists(userId, buildTimestamp(date, time))) {
                requestContent.setSessionAttribute(RequestParameter.ORDER_EXISTS, true);
                router.setRedirect(true);
                router.setPage(PageAddress.ADD_ORDER_PAGE);
            } else {
                requestContent.setSessionAttribute(RequestParameter.ORDER_EXISTS, false);
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
                requestContent.setSessionAttribute(RequestParameter.ACTIVITY_LIST, null);
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_ORDER_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }

    private Timestamp buildTimestamp(String date, String time) {
        String orderTime = date + " " + time;
        if (StringUtils.countMatches(orderTime, ":") == 1) {
            orderTime += ":00";
        }
        logger.log(Level.INFO, "order time before adding to db: "+orderTime);
        return Timestamp.valueOf(orderTime.replace("T", " "));
    }


    private static String getUTCTimestamp(String date, String time) {
        String orderTime = date + " " + time;
        if (StringUtils.countMatches(orderTime, ":") == 1) {
            orderTime += ":00";
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        final TimeZone utc = TimeZone.getTimeZone("UTC");
        dateFormatter.setTimeZone(utc);

        return dateFormatter.format(LocalDateTime.parse(orderTime));
    }

    public static Date dateToUTC(Date date){
        return new Date(date.getTime() -
                Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }
}
