package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;

public interface Command {
    PageRouter execute(SessionRequestContent requestContent);
}
