package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.ActivityValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class AddActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();


    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String name = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
            String description = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
            String price = requestContent.getParameter(RequestParameter.ACTIVITY_PRICE);
            boolean nameExists = service.nameExists(name);
            if (nameExists) {
                requestContent.setAttribute(RequestParameter.DATA_EXISTS, true);
                router.setPage(PageAddress.ADD_ACTIVITY_PAGE);
            } else {
                if (service.addActivity(name, description, price)) {
                    router.setRedirect(true);
                    router.setPage(PageAddress.VIEW_ACTIVITIES);
                } else {
                    logger.log(Level.ERROR, "Error while adding activity");
                }
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
