package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.controller.PageRouter;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;
import by.epam.web.service.ServiceFactory;
import by.epam.web.service.UserService;
import by.epam.web.controller.SessionRequestContent;
import by.epam.web.validation.NumberValidator;
import by.epam.web.validation.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class AddMoneyToCardCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {

            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            String cardNumber = requestContent.getParameter(RequestParameter.CARD_NUMBER);
            String moneyToAdd = requestContent.getParameter(RequestParameter.MONEY);
            if (service.findUserByIdAndCard(user.getId(), cardNumber).isPresent()) {
                if (service.addMoneyToCard(cardNumber, moneyToAdd)) {
                    requestContent.setAttribute(RequestParameter.OPERATION_SUCCESS, true);
                }
            } else {
                requestContent.setAttribute(RequestParameter.NO_CARD_FOUND, true);
            }
            router.setPage(PageAddress.ADD_MONEY_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
