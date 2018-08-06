package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import by.epam.web.validation.ActivityValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class AddActivityCommand implements Command{
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent){
        PageRouter router = new PageRouter();
        try {

            String name = requestContent.getParameter(RequestParameter.ACTIVITY_NAME);
            String description = requestContent.getParameter(RequestParameter.ACTIVITY_DESCRIPTION);
            String price = requestContent.getParameter(RequestParameter.ACTIVITY_PRICE);

            if (validateActivity(requestContent, name, description, price)){

                boolean nameExists = service.nameExists(name);
                if (nameExists) {
                    requestContent.setAttribute(RequestParameter.ACTIVITY_EXISTS, true);
                    router.setTransitionType(PageRouter.TransitionType.FORWARD);
                    router.setPage(PageAddress.VIEW_ACTIVITIES);
                } else {
                    service.addActivity(name, description, new BigDecimal(price));
                    router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                    router.setPage(PageAddress.VIEW_ACTIVITIES);
                }
            } else {
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.VIEW_ACTIVITIES);
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

    boolean validateActivity(SessionRequestContent requestContent,
                                    String name, String description, String price){
        boolean flag = true;
        if (!ActivityValidator.getInstance().validateName(name)){
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_ACTIVITY_NAME, true);
        }
        if (!ActivityValidator.getInstance().validateDescription(description)){
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_ACTIVITY_DESCRIPTION, true);
        }
        if (!ActivityValidator.getInstance().validatePrice(price)){
            flag = false;
            requestContent.setAttribute(RequestParameter.ILLEGAL_ACTIVITY_PRICE, true);
        }
        return flag;
    }
}
