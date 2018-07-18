package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.constant.JspAddress;
import by.epam.web.constant.JspParameter;
import by.epam.web.util.NoSuchRequestParameterException;
import by.epam.web.util.SessionRequestContent;

public interface Command {
    PageRouter execute(SessionRequestContent requestContent) throws CommandException;
}
