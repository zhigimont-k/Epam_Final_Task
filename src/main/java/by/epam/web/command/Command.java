package by.epam.web.command;

import by.epam.web.controller.PageRouter;
import by.epam.web.util.SessionRequestContent;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Command {
    PageRouter execute(SessionRequestContent requestContent) throws IOException, ServletException;
}
