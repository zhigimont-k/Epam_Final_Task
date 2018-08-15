package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.util.MailComposer;
import by.epam.web.util.MailSenderThread;
import by.epam.web.util.PasswordGenerator;
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;


public class ResetPasswordCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * Retrieves email from request parameters, looks for a user with this email in the database
     * If he doesn't exists, shows error message
     * If he does, changes this user's password to a new generated one and sends an email with it
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
            String email = requestContent.getParameter(RequestParameter.EMAIL);
            Optional<User> found = service.findUserByEmail(email);
            if (found.isPresent()) {
                String newPassword = PasswordGenerator.generatePassword();
                service.updateUser(found.get().getId(), newPassword,
                        found.get().getUserName());
                found.get().setPassword(newPassword);
                requestContent.setSessionAttribute(RequestParameter.OPERATION_SUCCESS,
                        true);
                router.setRedirect(true);
                new MailSenderThread(email, MailComposer.getResetPasswordMessageTheme(),
                        MailComposer.getResetPasswordMessage(newPassword)).start();
            } else {
                requestContent.setAttribute(RequestParameter.NO_EMAIL_FOUND, true);
            }
            router.setPage(PageAddress.RESET_PASSWORD_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setRedirect(false);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
