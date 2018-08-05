package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.controller.PageRouter;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PayForOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            OrderService service = ServiceFactory.getInstance().getOrderService();
            String id = requestContent.getParameter(RequestParameter.ORDER_ID);
            int orderId = Integer.parseInt(id);
            List<Order> userOrders = service.findOrdersByUser(user.getId());
            boolean orderByUserExists = false;
            for (Order order : userOrders){
                if (order.getId() == orderId){
                    orderByUserExists = true;
                }
            }
            if (orderByUserExists){
                service.payForOrder(Integer.parseInt(id));
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.VIEW_USER_ORDERS);
            } else {
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(PageAddress.FORBIDDEN_ERROR_PAGE);
            }

        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
