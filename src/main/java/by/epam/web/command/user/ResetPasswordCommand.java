package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.controller.constant.JspAddress;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.util.mail.MailComposer;
import by.epam.web.util.mail.MailSenderThread;
import by.epam.web.util.mail.Settings;
import by.epam.web.util.passwordgenerator.PasswordGenerator;
import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;


public class ResetPasswordCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String email = requestContent.getParameter(JspParameter.EMAIL);
            UserService service = ServiceFactory.getInstance().getUserService();
            Optional<User> found = service.findUserByEmail(email);
            if (found.isPresent()) {

                String newPassword = PasswordGenerator.generatePassword();

                found.get().setPassword(newPassword);
                logger.log(Level.INFO, "Resetting password for: "+found.get());
                service.updateUser(found.get());


                Settings settings = new Settings();
                settings.setHostValue(Settings.HOST_VALUE);
                settings.setUserValue(Settings.USER_VALUE);
                settings.setPasswordValue(Settings.PASSWORD_VALUE);
                settings.setProtocolValue(Settings.PROTOCOL_VALUE);
                try {
                    int portValue = Integer.parseInt(Settings.PORT);
                    settings.setPortValue(portValue);
                } catch (NumberFormatException ex) {
                    logger.log(Level.ERROR, ex);
                    settings.setPortValue(Settings.DEFAULT_PORT);
                }
                new MailSenderThread(email, MailComposer.getResetPasswordMessageTheme(),
                        MailComposer.getResetPasswordMessage(newPassword), settings).start();


                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.HOME_PAGE);
            } else {
                requestContent.setAttribute(JspParameter.AUTH_FAIL, true);
                router.setTransitionType(PageRouter.TransitionType.FORWARD);
                router.setPage(JspAddress.LOGIN_PAGE);
            }
        } catch (NoSuchRequestParameterException e) {
            logger.log(Level.ERROR, e);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            requestContent.setAttribute(JspParameter.ERROR_MESSAGE, e.getMessage());
            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.ERROR_PAGE);
        }
        return router;
    }
}
