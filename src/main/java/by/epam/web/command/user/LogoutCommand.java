package by.epam.web.command.user;

import by.epam.web.command.Command;
import by.epam.web.controller.PageRouter;
import by.epam.web.constant.PageAddress;
import by.epam.web.controller.SessionRequestContent;

public class LogoutCommand implements Command {

    /**
     * Redirects to the home page
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        router.setRedirect(true);
        router.setPage(PageAddress.HOME_PAGE);
        return router;
    }
}
