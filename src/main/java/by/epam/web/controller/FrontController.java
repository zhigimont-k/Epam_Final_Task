package by.epam.web.controller;

import by.epam.web.command.Command;
import by.epam.web.command.CommandFactory;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.SessionRequestContent;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

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
        request.setCharacterEncoding("UTF-8");
        PageRouter router = new PageRouter();
        SessionRequestContent requestContent = new SessionRequestContent(request);
        requestContent.insertValues(request);
        String commandName = request.getParameter(JspParameter.COMMAND);

        Optional<Command> foundCommand = CommandFactory.getInstance().defineCommand(commandName);

        try {
            if (foundCommand.isPresent()) {
                Command command = foundCommand.get();
                router = command.execute(requestContent);
                if (router != null) {
                    if (PageRouter.TransitionType.FORWARD.equals(router.getTransitionType())) {
                        RequestDispatcher dispatcher = request.getRequestDispatcher(router.getPage());
                        dispatcher.forward(request, response);
                    } else {
                        response.sendRedirect(router.getPage());
                    }
                }
            }
        } catch (CommandException e) {
            requestContent.setAttribute(JspAttribute.ERROR_MESSAGE, e.getMessage());

            router.setTransitionType(PageRouter.TransitionType.FORWARD);
            router.setPage(JspAddress.ERROR_PAGE);
        }
    }
}
