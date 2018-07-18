package by.epam.web.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UploadFile", urlPatterns = {"/upload"})
@MultipartConfig(location = "/upload", maxFileSize = 10485760L)
public class UploadFileController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
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
