package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.Activity;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.NoSuchRequestParameterException;
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

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {

        PageRouter router = new PageRouter();
        try {

            ActivityService service = ServiceFactory.getInstance().getActivityService();
            String id = requestContent.getParameter(RequestParameter.ACTIVITY_ID);
            if (NumberValidator.getInstance().validateId(id)) {
                String newName = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
                String newDescription = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
                String newPrice = requestContent.getParameter(RequestParameter.ACTIVITY_PRICE);
                String newStatus = requestContent.getParameter(RequestParameter.ACTIVITY_STATUS);

                if (validateActivity(requestContent, newName, newDescription, newPrice, newStatus)) {
                    boolean nameExists = service.nameExists(newName);
                    if (nameExists) {
                        requestContent.setAttribute(RequestParameter.ACTIVITY_EXISTS, true);
                        router.setTransitionType(PageRouter.TransitionType.FORWARD);
                        router.setPage(PageAddress.VIEW_ACTIVITIES);
                    } else {
                        Optional<Activity> found = service.findActivityById(Integer.parseInt(id));
                        if (found.isPresent()) {
                            found.get().setName(newName);
                            found.get().setDescription(newDescription);
                            found.get().setPrice(new BigDecimal(newPrice));
                            found.get().setStatus(newStatus);
                            service.updateActivity(found.get());
                            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                            router.setPage(PageAddress.VIEW_ACTIVITIES);

                        } else {
                            router.setTransitionType(PageRouter.TransitionType.FORWARD);
                            router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                        }
                    }
                } else {
                    router.setTransitionType(PageRouter.TransitionType.FORWARD);
                    router.setPage(PageAddress.EDIT_ACTIVITY_PAGE);
                }
            } else {
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
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

    private boolean validateActivity(SessionRequestContent requestContent,
                                     String name, String description, String price, String status) {
        boolean flag = true;
        if (!ActivityValidator.getInstance().validateName(name)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_ACTIVITY_NAME, true);
        }
        if (!ActivityValidator.getInstance().validateDescription(description)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_ACTIVITY_DESCRIPTION, true);
        }
        if (!ActivityValidator.getInstance().validatePrice(price)) {
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_ACTIVITY_PRICE, true);
        }
        return flag && ActivityValidator.getInstance().validateStatus(status);
    }
}
