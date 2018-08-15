package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Order;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import by.epam.web.validation.OrderValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ChangeOrderStatusCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();

    /**
     * Retrieves order's ID and new status from request parameters, changes order status
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
            String id = requestContent.getParameter(RequestParameter.ORDER_ID);
            String status = requestContent.getParameter(RequestParameter.ORDER_STATUS);
            Optional<Order> found = service.findOrderById(Integer.parseInt(id));
            if (found.isPresent()){
                service.changeOrderStatus(Integer.parseInt(id), status);
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_ALL_ORDERS);
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
