package by.epam.web.command.admin;

import by.epam.web.command.Command;
import by.epam.web.command.CommandException;
import by.epam.web.constant.JspAddress;
import by.epam.web.constant.JspAttribute;
import by.epam.web.constant.JspParameter;
import by.epam.web.controller.PageRouter;
import by.epam.web.entity.Activity;
import by.epam.web.service.ActivityService;
import by.epam.web.service.ServiceException;
import by.epam.web.util.NoSuchRequestParameterException;
import by.epam.web.util.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.math.BigDecimal;

public class AddActivityCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public PageRouter execute(SessionRequestContent requestContent) throws CommandException{
        PageRouter router = new PageRouter();
        try {

            String name = requestContent.getParameter(JspParameter.SERVICE_NAME);
            String description = requestContent.getParameter(JspParameter.SERVICE_DESCRIPTION);
//            BigDecimal price = BigDecimal.(requestContent.getParameter(JspParameter.SERVICE_PRICE));
//            Part image = requestContent.getPart(JspParameter.IMAGE);
            ActivityService service = new ActivityService();

            boolean nameExists = service.nameExists(name);
            if (nameExists) {
                logger.log(Level.INFO, "Service " + name + " exists");
                requestContent.setAttribute(JspAttribute.LOGIN_EXISTS, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.REGISTER_PAGE);
            } else{
                Activity activity = service.addActivity("","",new BigDecimal(0));
                router.setTransitionType(PageRouter.TransitionType.REDIRECT);
                router.setPage(JspAddress.SERVICES_PAGE);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
