package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Order;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();

    /**
     * Retrieves added order from session attributes, adds it to the database and removes it
     * from session attributes
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
            Order order = (Order)requestContent.getSessionAttribute(RequestParameter.ORDER);
            service.addOrder(String.valueOf(order.getUserId()), order.getDateTime(),
                    order.getActivityList());
            requestContent.removeSessionAttribute(RequestParameter.ORDER);
            router.setRedirect(true);
            router.setPage(PageAddress.VIEW_USER_ORDERS);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
