package by.epam.web.dao.user.impl;

import by.epam.web.dao.connection.ConnectionPool;
import by.epam.web.dao.connection.ConnectionPoolException;
import by.epam.web.dao.connection.ProxyConnection;
import by.epam.web.dao.DAOException;
import by.epam.web.dao.user.UserDAO;
import by.epam.web.dao.constant.QueryName;
import by.epam.web.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserDAOImpl implements UserDAO {
    private final ConnectionPool pool = ConnectionPool.getInstance();

    public boolean register(User user) throws DAOException {
        ProxyConnection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.getConnection();
            String login = user.getLogin();
            String password = user.getPassword();

            if (userExists(connection, login)){
                return false;
            }

            ResourceBundle bundle = ResourceBundle.getBundle("query.user");
            String registerQuery = bundle.getString(QueryName.REGISTER_USER);
            preparedStatement = connection.prepareStatement(registerQuery);


            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int userID = resultSet.getInt(1);

            user.setId(userID);

            return true;
        } catch (SQLException e) {
            throw new DAOException("Failed to register user", e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DAOException(e);
            }
        }
    }

    public boolean login(User user) throws DAOException {
        ProxyConnection connection = null;
        try {
            connection = pool.getConnection();
            String login = user.getLogin();
            String password = user.getPassword();

            return passwordMatches(connection, login, password);
        } catch (SQLException e) {
            throw new DAOException("Failed to login user", e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DAOException(e);
            }
        }
    }

    private boolean userExists(ProxyConnection connection, String login) throws DAOException{
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {

            ResourceBundle bundle = ResourceBundle.getBundle("query.user");
            String checkPasswordQuery = bundle.getString(QueryName.CHECK_USER_EXISTS);

            preparedStatement = connection.prepareStatement(checkPasswordQuery);
            preparedStatement.setString(1, login);

            resultSet = preparedStatement.executeQuery();

            return !resultSet.next();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DAOException(e);
            }
        }
    }

    private boolean passwordMatches(ProxyConnection connection, String login, String password) throws DAOException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("query.user");
            String checkPasswordQuery = bundle.getString(QueryName.CHECK_PASSWORD_MATCH);

            preparedStatement = connection.prepareStatement(checkPasswordQuery);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DAOException(e);
            }
        }
    }
}
