package by.epam.web.controller;

import by.epam.web.controller.command.Command;
import by.epam.web.controller.command.CommandFactory;
import by.epam.web.controller.constant.JspParameter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FrontController", urlPatterns = {"/app"})
public class FrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String commandName = request.getParameter(JspParameter.COMMAND);

        Command command = CommandFactory.getInstance().defineCommand(commandName);
        command.execute(request, response);
        //request.getRequestDispatcher(JspAddress.ERROR).forward(request, response);
    }
}
