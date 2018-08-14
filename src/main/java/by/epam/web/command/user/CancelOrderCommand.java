package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CancelOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();

    /**
     * Retrieves user and cancelled order's ID from request and session parameters and
     * cancels order
     *
     * @param requestContent
     * Request and session parameters and attributes
     * @return
     * Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            String id = requestContent.getParameter(RequestParameter.ORDER_ID);
            Optional<Order> found = service.findOrderById(id);
            if (found.isPresent()) {
                if (user.getId() == found.get().getUserId()) {
                    service.cancelOrder(Integer.parseInt(id));
                    router.setRedirect(true);
                    router.setPage(PageAddress.VIEW_USER_ORDERS);
                } else {
                    router.setPage(PageAddress.FORBIDDEN_ERROR_PAGE);
                }
            } else {
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
