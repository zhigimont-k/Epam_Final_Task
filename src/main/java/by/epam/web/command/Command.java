package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.controller.SessionRequestContent;

public interface Command {
    /**
     *
     * @param requestContent
     * Request and session parameters and attributes
     * @return
     * Address of the next page
     */
    PageRouter execute(SessionRequestContent requestContent);
}
