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

    /**
     * Retrieves user's ID and order's properties from session and request
     * If order by the same user with the same time exists or date\time is invalid, shows error message
     * Otherwise calculates order's price, creates new order, sets it as session attribute
     * and redirects to order confirmation page
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
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
            if (!OrderValidator.getInstance().validateDate(date) ||
                    !OrderValidator.getInstance().validateTime(time, date)) {
                requestContent.setSessionAttribute(RequestParameter.ILLEGAL_INPUT, true);
                router.setRedirect(true);
                router.setPage(PageAddress.ADD_ORDER_PAGE);
            } else if (service.orderExists(userId, date, time)){
                requestContent.setSessionAttribute(RequestParameter.ORDER_EXISTS, true);
                router.setRedirect(true);
                router.setPage(PageAddress.ADD_ORDER_PAGE);
            } else {
                requestContent.setSessionAttribute(RequestParameter.ILLEGAL_INPUT, false);
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
                order.setDateTime(service.buildTimestamp(date, time));
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
}
