package by.epam.web.controller;

import by.epam.web.command.Command;
import by.epam.web.command.factory.CommandFactory;
import by.epam.web.controller.constant.JspParameter;
import by.epam.web.util.SessionRequestContent;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = { "/upload" })
@MultipartConfig(location = "/upload", maxFileSize = 10485760L)
public class UploadFileController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
