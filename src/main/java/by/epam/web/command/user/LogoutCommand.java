package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.util.content.SessionRequestContent;

public class LogoutCommand implements Command {

    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        router.setRedirect(true);
        router.setPage(PageAddress.HOME_PAGE);
        return router;
    }
}
