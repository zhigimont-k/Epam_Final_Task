package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.util.content.SessionRequestContent;

public interface Command {
    PageRouter execute(SessionRequestContent requestContent);
}
