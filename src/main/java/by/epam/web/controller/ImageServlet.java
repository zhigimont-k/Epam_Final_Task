package by.epam.web.controller;

import by.epam.web.constant.PageAddress;
import by.epam.web.constant.RequestParameter;
import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.PoolException;
import by.epam.web.pool.ProxyConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "ImageServlet", urlPatterns = {"/image"})
@MultipartConfig(maxFileSize = 16177215) //16MB
public class ImageServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool pool = ConnectionPool.getInstance();
    private static final String FIND_IMAGE_BY_USER_ID = "SELECT image, image_name " +
            "FROM user WHERE user_id = ?";
    private static final String UPDATE_USER_IMAGE = "UPDATE user " +
            "SET image = ?, image_name = ? WHERE user_id = ?";
    private static final String DB_USER_IMAGE_FIELD = "image";
    private static final String DB_USER_IMAGE_NAME_FIELD = "image_name";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
    private static final String CONTENT_DISPOSITION_HEADER_VALUE = "inline; filename=\"";
    private static final String BACK_SLASH = "\"";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter(RequestParameter.USER_ID));
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            byte[] imageData = {};
            String imageFileName = "";
            preparedStatement = connection.prepareStatement(FIND_IMAGE_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                imageData = resultSet.getBytes(DB_USER_IMAGE_FIELD);
                imageFileName = resultSet.getString(DB_USER_IMAGE_NAME_FIELD);
            }
            String contentType = this.getServletContext().getMimeType(imageFileName);
            response.setHeader(CONTENT_TYPE_HEADER, contentType);
            response.setHeader(CONTENT_LENGTH_HEADER, String.valueOf(imageData.length));
            response.setHeader(CONTENT_DISPOSITION_HEADER,
                    CONTENT_DISPOSITION_HEADER_VALUE+ imageFileName + BACK_SLASH);
            response.getOutputStream().write(imageData);

        } catch (PoolException e) {
            logger.fatal(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter(RequestParameter.USER_ID));
        InputStream inputStream = null;
        Part filePart = request.getPart(RequestParameter.PHOTO);
        String fileName = getFileName(filePart);
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        if (filePart != null) {
            logger.log(Level.INFO, filePart.getName());
            logger.log(Level.INFO, filePart.getSize());
            logger.log(Level.INFO, filePart.getContentType());
            inputStream = filePart.getInputStream();
        }
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_USER_IMAGE);
            if (inputStream != null) {
                preparedStatement.setBlob(1, inputStream);
            }
            preparedStatement.setString(2, fileName);
            preparedStatement.setInt(3, userId);
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                logger.log(Level.INFO, "File uploaded and saved into database");
            }
            response.sendRedirect(PageAddress.VIEW_USER_INFO);
        } catch (PoolException e) {
            logger.fatal(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader(CONTENT_DISPOSITION_HEADER).split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace(BACK_SLASH, "");
            }
        }
        return "";
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Can't close statement: " + e.getMessage(), e);
            }
        }
    }
}