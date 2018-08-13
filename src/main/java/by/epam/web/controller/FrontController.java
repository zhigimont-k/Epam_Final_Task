package by.epam.web.controller;

import by.epam.web.command.Command;
import by.epam.web.command.CommandAccessLevel;
import by.epam.web.command.CommandFactory;
import by.epam.web.command.common.RegisterCommand;
import by.epam.web.command.user.LoginCommand;
import by.epam.web.command.user.ResetPasswordCommand;
import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet(name = "FrontController", urlPatterns = {"/app"})
public class FrontController extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    private enum GetMethodCommand {
        VIEW_USERS("viewUsers"),
        VIEW_USER_INFO("viewUserInfo"),
        VIEW_ACTIVITY("viewActivity"),
        VIEW_ACTIVITIES("viewActivities"),
        VIEW_ORDER("viewOrder"),
        VIEW_USER_ORDERS("viewUserOrders"),
        VIEW_ALL_ORDERS("viewAllOrders"),
        CREATE_ORDER("createOrder"),
        LOGOUT("logout");

        private String commandName;

        GetMethodCommand(String value) {
            commandName = value;
        }

        public String getName() {
            return commandName;
        }

        public static boolean isGetMethodCommand(String commandName){
            return Arrays.stream(GetMethodCommand.values())
                    .anyMatch(command -> command.getName().equalsIgnoreCase(commandName));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter(RequestParameter.COMMAND);
        if (GetMethodCommand.isGetMethodCommand(commandName)) {
            processRequest(request, response);
        } else {
            logger.log(Level.ERROR, "Tried to call "+commandName+" command through GET method");
            response.sendRedirect(PageAddress.HOME_PAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter(RequestParameter.COMMAND);
        Optional<Command> foundCommand = CommandFactory.getInstance().defineCommand(commandName);
        if (foundCommand.isPresent()) {
            Command command = foundCommand.get();
            SessionRequestContent requestContent = new SessionRequestContent(request);
            PageRouter router = command.execute(requestContent);
            requestContent.insertValues(request);
            if (router != null) {
                if (router.getRedirect()) {
                    response.sendRedirect(router.getPage());
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher(router.getPage());
                    dispatcher.forward(request, response);
                }
            }
        }
    }
}