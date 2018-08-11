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
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.service.UserService;
import by.epam.web.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PayForOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();
    private static UserService userService = ServiceFactory.getInstance().getUserService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            String id = requestContent.getParameter(RequestParameter.ORDER_ID);
            int orderId = Integer.parseInt(id);
            if (service.findOrderById(orderId).get().getPrice().compareTo(
                    userService.findMoneyByCardNumber(user.getCardNumber())
            ) <= 0){
                requestContent.setSessionAttribute("notEnoughMoney", false);
                service.payForOrder(Integer.parseInt(id));
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_USER_ORDERS);
            } else {
                requestContent.setSessionAttribute("notEnoughMoney", true);
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_USER_ORDERS);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
