package by.epam.web.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EmptyCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {

    }
}
