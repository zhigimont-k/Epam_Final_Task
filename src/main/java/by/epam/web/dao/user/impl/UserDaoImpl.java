package by.epam.web.dao.user.impl;

import by.epam.web.dao.connection.ConnectionPool;
import by.epam.web.dao.connection.ConnectionPoolException;
import by.epam.web.dao.connection.ProxyConnection;
import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.UserDao;
import by.epam.web.dao.constant.QueryName;
import by.epam.web.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserDaoImpl implements UserDao {
    private static Logger logger = LogManager.getLogger();
    private final ConnectionPool pool = ConnectionPool.getInstance();

    public boolean register(User user) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        try {
            connection = pool.getConnection();
            String login = user.getLogin();
            String password = user.getPassword();
            String email = user.getEmail();
            String phoneNumber = user.getPhoneNumber();
            String userName = user.getName();

            if (userExists(connection, login)){
                return false;
            }

            ResourceBundle bundle = ResourceBundle.getBundle("query.user");
            String registerQuery = bundle.getString(QueryName.REGISTER_USER);
            preparedStatement = connection.prepareStatement(registerQuery, Statement.RETURN_GENERATED_KEYS);


            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, userName);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()){
                int userID = resultSet.getInt(1);
                user.setId(userID);
            } else {
//                connection.rollback();
                throw new DaoException("Couldn't retrieve user's ID");
            }


            return true;
        } catch (SQLException e) {
            throw new DaoException("Failed to register user", e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public boolean login(User user) throws DaoException, IncorrectPasswordException, NoSuchUserException {
        ProxyConnection connection = null;
        try {
            connection = pool.getConnection();
            String login = user.getLogin();
            String password = user.getPassword();

            if (userExists(connection, login)){
                throw new NoSuchUserException();
            }

            if (!passwordMatches(connection, login, password)){
                throw new IncorrectPasswordException();
            }
            return true;
        } catch (SQLException e) {
            throw new DaoException("Failed to login user", e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    private boolean userExists(ProxyConnection connection, String login) throws DaoException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {

            ResourceBundle bundle = ResourceBundle.getBundle("query.user");
            String checkPasswordQuery = bundle.getString(QueryName.CHECK_USER_EXISTS);

            preparedStatement = connection.prepareStatement(checkPasswordQuery);
            preparedStatement.setString(1, login);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    private boolean passwordMatches(ProxyConnection connection, String login, String password) throws DaoException {
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
            throw new DaoException(e);
        } finally {
            try {
                pool.releaseConnection(connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }
}
