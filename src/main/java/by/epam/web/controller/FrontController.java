package by.epam.web.controller;

import by.epam.web.command.Command;
import by.epam.web.command.CommandFactory;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.sessionrequestcontent.SessionRequestContent;

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

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String HTML_CONTENT_TYPE = "text/html";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(HTML_CONTENT_TYPE);
        request.setCharacterEncoding(UTF_8_ENCODING);
        String commandName = request.getParameter(JspParameter.COMMAND);

        Optional<Command> foundCommand = CommandFactory.getInstance().defineCommand(commandName);

        if (foundCommand.isPresent()) {
            Command command = foundCommand.get();
            SessionRequestContent requestContent = new SessionRequestContent(request);
            PageRouter router = command.execute(requestContent);
            requestContent.insertValues(request);
            if (router != null) {
                if(PageRouter.TransitionType.FORWARD.equals(router.getTransitionType())) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher(router.getPage());
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect(router.getPage());
                }
            }
        }
    }
}