//package by.epam.web.command.user;
//
//import by.epam.web.command.Command;
//import by.epam.web.constant.PageAddress;
//import by.epam.web.constant.RequestParameter;
//import by.epam.web.controller.PageRouter;
//import by.epam.web.entity.User;
//import by.epam.web.service.ServiceException;
//import by.epam.web.service.ServiceFactory;
//import by.epam.web.service.UserService;
//import by.epam.web.util.sessionrequestcontent.NoSuchRequestParameterException;
//import by.epam.web.util.sessionrequestcontent.SessionRequestContent;
//import org.apache.logging.log4j.Level;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.math.BigDecimal;
//
//public class AddMoneyToCardCommand implements Command {
//    private static Logger logger = LogManager.getLogger();
//
//    @Override
//    public PageRouter execute(SessionRequestContent requestContent) {
//        PageRouter router = new PageRouter();
//        try {
//
//            UserService service = ServiceFactory.getInstance().getUserService();
//            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
//            String cardNumber = requestContent.getParameter("cardNumber");
//            String moneyToAdd = requestContent.getParameter("moneyToAdd");
//            if (service.findUserByIdAndCard(user.getId(), Integer.parseInt(cardNumber)).isPresent()) {
//                user = service.updateUserCard(user.getId(),
//                        BigDecimal.valueOf(Double.parseDouble(moneyToAdd))).get();
//            } else {
//                requestContent.setAttribute(RequestParameter.AUTH_FAIL, true);
//                router.setTransitionType(PageRouter.TransitionType.FORWARD);
//                router.setPage("addMoneyPage");
//            }
//
//            requestContent.setSessionAttribute(RequestParameter.USER, user);
//
//            router.setTransitionType(PageRouter.TransitionType.REDIRECT);
//            router.setPage(PageAddress.ACCOUNT_PAGE);
//        } catch (NoSuchRequestParameterException e) {
//            logger.log(Level.ERROR, e);
//        } catch (ServiceException e) {
//            logger.log(Level.ERROR, e);
//            requestContent.setAttribute(RequestParameter.ERROR_MESSAGE, e.getMessage());
//            router.setTransitionType(PageRouter.TransitionType.FORWARD);
//            router.setPage(PageAddress.ERROR_PAGE);
//        }
//        return router;
//    }
//}
