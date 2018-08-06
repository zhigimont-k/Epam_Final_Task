package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Order;
import by.epam.web.service.OrderService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ViewOrdersCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();
    private static final int RECORDS_PER_PAGE = 10;

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String pageNumberParameter = requestContent.getParameter(RequestParameter.PAGE_NUMBER);
            if (NumberValidator.getInstance().validateId(pageNumberParameter)){
                int pageNumber = Integer.parseInt(requestContent.getParameter
                        (RequestParameter.PAGE_NUMBER));

                int numberOfRecords = service.countOrders();
                int numberOfPages = (int) Math.ceil(numberOfRecords * 1.0 / RECORDS_PER_PAGE);

                if (pageNumber > numberOfPages){
                    pageNumber = numberOfPages;
                }
                List<Order> orderList = service.findAllOrders(
                        (pageNumber - 1) * RECORDS_PER_PAGE,
                        RECORDS_PER_PAGE);

                requestContent.setAttribute(RequestParameter.ORDER_LIST, orderList);
                requestContent.setAttribute(RequestParameter.NUMBER_OF_PAGES, numberOfPages);
                requestContent.setAttribute(RequestParameter.CURRENT_TABLE_PAGE_NUMBER, pageNumber);

                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.ALL_ORDERS_PAGE);
            } else {
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
            }

        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
