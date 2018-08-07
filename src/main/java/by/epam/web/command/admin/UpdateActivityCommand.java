package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.SessionRequestContent;
import by.epam.web.validation.ActivityValidator;
import by.epam.web.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Optional;

public class UpdateActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {

        PageRouter router = new PageRouter();
        try {

            String id = requestContent.getParameter(RequestParameter.ACTIVITY_ID);
            if (id.isEmpty()) {
                id = (String) requestContent.getSessionAttribute(RequestParameter.ACTIVITY_ID);
            }
            if (NumberValidator.getInstance().validateId(id)) {
                requestContent.setSessionAttribute(RequestParameter.ACTIVITY_ID, id);
                String newName = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
                String newDescription = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
                String newPrice = requestContent.getParameter(RequestParameter.ACTIVITY_PRICE);
                String newStatus = requestContent.getParameter(RequestParameter.ACTIVITY_STATUS);

                if (validateActivity(newName, newDescription, newPrice, newStatus)) {
                    boolean nameExists = service.nameExists(newName);
                    if (nameExists) {
                        requestContent.setSessionAttribute(RequestParameter.DATA_EXISTS, true);
                        router.setRedirect(true);
                        router.setPage(PageAddress.VIEW_ACTIVITIES);
                    } else {
                        requestContent.removeSessionAttribute(RequestParameter.DATA_EXISTS);
                        Optional<Activity> found = service.findActivityById(Integer.parseInt(id));
                        if (found.isPresent()) {
                            found.get().setName(newName);
                            found.get().setDescription(newDescription);
                            found.get().setPrice(new BigDecimal(newPrice));
                            found.get().setStatus(newStatus);
                            service.updateActivity(found.get());
                            router.setRedirect(true);
                            router.setPage(PageAddress.VIEW_ACTIVITIES);

                        } else {
                            router.setRedirect(false);
                            router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                        }
                    }
                } else {
                    router.setRedirect(true);
                    router.setPage(PageAddress.EDIT_ACTIVITY_PAGE);
                }
            } else {
                router.setRedirect(false);
                router.setPage(PageAddress.BAD_REQUEST_ERROR_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setRedirect(false);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }

    private boolean validateActivity(String name, String description, String price, String status) {
        return ActivityValidator.getInstance().validateName(name) &&
                ActivityValidator.getInstance().validateDescription(description) &&
                ActivityValidator.getInstance().validatePrice(price) &&
                ActivityValidator.getInstance().validateStatus(status);
    }
}
