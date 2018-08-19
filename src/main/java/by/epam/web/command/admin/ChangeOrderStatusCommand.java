package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeOrderStatusCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();

    /**
     * Retrieves order's ID and new status from request parameters, changes order status
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String id = requestContent.getParameter(RequestParameter.ORDER_ID);
            String status = requestContent.getParameter(RequestParameter.ORDER_STATUS);
            if (!service.changeOrderStatus(Integer.parseInt(id), status)) {
                logger.log(Level.ERROR, "Couldn't change order status");
            }
            router.setRedirect(true);
            router.setPage(PageAddress.VIEW_ALL_ORDERS);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
