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
import by.epam.web.util.request.SessionRequestContent;
import by.epam.web.validation.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;


public class ResetPasswordCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String email = requestContent.getParameter(RequestParameter.EMAIL);
            if (UserValidator.getInstance().validateEmail(email)){
                Optional<User> found = service.findUserByEmail(email);
                if (found.isPresent()) {
                    String newPassword = PasswordGenerator.generatePassword();
                    service.updateUser(found.get().getId(), newPassword, found.get().getUserName());
                    found.get().setPassword(newPassword);

                    new MailSenderThread(email, MailComposer.getResetPasswordMessageTheme(),
                            MailComposer.getResetPasswordMessage(newPassword)).start();

                    router.setRedirect(false);
                    router.setPage(PageAddress.HOME_PAGE);
                } else {
                    requestContent.setAttribute(RequestParameter.NO_EMAIL_FOUND, true);
                    router.setRedirect(false);
                    router.setPage(PageAddress.LOGIN_PAGE);
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
}
