package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.util.mail.MailComposer;
import by.epam.web.util.mail.MailSenderThread;
import by.epam.web.util.password.PasswordGenerator;
import by.epam.web.util.request.NoSuchRequestParameterException;
import by.epam.web.util.request.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;


public class ResetPasswordCommand implements Command {
    private static Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String email = requestContent.getParameter(RequestParameter.EMAIL);
            UserService service = ServiceFactory.getInstance().getUserService();
            Optional<User> found = service.findUserByEmail(email);
            if (found.isPresent()) {

                String newPassword = PasswordGenerator.generatePassword();

                logger.log(Level.INFO, "Resetting password for: "+found.get());
                service.updateUser(found.get().getId(), newPassword, found.get().getUserName());
                found.get().setPassword(newPassword);

                new MailSenderThread(email, MailComposer.getResetPasswordMessageTheme(),
                        MailComposer.getResetPasswordMessage(newPassword)).start();


                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.HOME_PAGE);
            } else {
                requestContent.setAttribute(RequestParameter.AUTH_FAIL, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(PageAddress.LOGIN_PAGE);
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
}
