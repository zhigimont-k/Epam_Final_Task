package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.JspAddress;
import by.epam.web.constant.JspAttribute;
import by.epam.web.util.SessionRequestContent;

public class LogoutCommand implements Command {

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        requestContent.removeSessionAttribute(JspAttribute.USER);
        router.setTransitionType(PageRouter.TransitionType.REDIRECT);
        router.setPage(JspAddress.HOME_PAGE);
        return router;
    }
}
