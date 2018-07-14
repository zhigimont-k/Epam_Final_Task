package by.epam.web.command;

import by.epam.web.controller.PageRouter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {
    PageRouter execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
