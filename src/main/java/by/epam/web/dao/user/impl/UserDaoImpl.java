package by.epam.web.dao.user.impl;

import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.ConnectionPoolException;
import by.epam.web.pool.ProxyConnection;
import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.UserDao;
import by.epam.web.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDaoImpl implements UserDao {
    public enum UniqueUserInfo {
        LOGIN, EMAIL, PHONE_NUMBER
    }
    private static final ConnectionPool pool = ConnectionPool.getInstance();
    private static final Logger logger = LogManager.getLogger();

    private static final String REGISTER_USER_QUERY = "INSERT INTO user " +
            "(login, password, user_email, phone_number, user_name) " +
            "VALUES (?, SHA1(?), ?, ?, ?)";
    private static final String CHECK_LOGIN_EXISTS_QUERY = "SELECT user.user_id FROM user WHERE user.login = ?";
    private static final String CHECK_EMAIL_EXISTS_QUERY = "SELECT user.user_id FROM user WHERE user.user_email = ?";
    private static final String CHECK_PHONE_NUMBER_EXISTS_QUERY = "SELECT user.user_id FROM user WHERE user.phone_number = ?";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT user.user_id FROM user WHERE user.user_id = ?";
    private static final String FIND_USER_BY_LOGIN_AND_PASSWORD_QUERY = "SELECT user.user_id FROM user WHERE user.login = ?" +
            "AND user.password = SHA1(?)";

    public boolean register(User user) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.getConnection();
            String login = user.getLogin();
            String password = user.getPassword();
            String email = user.getEmail();
            String phoneNumber = user.getPhoneNumber();
            String userName = user.getUserName();

            preparedStatement = connection.prepareStatement(REGISTER_USER_QUERY, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, userName);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int userID = resultSet.getInt(1);
                user.setId(userID);
            } else {
//                connection.rollback(); //ПРОВЕРИТЬ!!!
                throw new DaoException("Couldn't retrieve user's ID");
            }

            logger.log(Level.INFO, "Registered user: " + user);

            return true;
        } catch (SQLException e) {
            throw new DaoException("Failed to register user", e);
        } finally {

            try {
                pool.releaseConnection(connection, preparedStatement);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public User login(User user) throws DaoException {
        ProxyConnection connection = null;

        try {
            connection = pool.getConnection();
            String login = user.getLogin();
            String password = user.getPassword();

            logger.log(Level.INFO, user + " logged in");
            return user;
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

    public boolean propertyExists(UniqueUserInfo property, String value) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            connection = pool.getConnection();
            switch(property){
                case LOGIN:
                    preparedStatement = connection.prepareStatement(CHECK_LOGIN_EXISTS_QUERY);
                    break;
                case EMAIL:
                    preparedStatement = connection.prepareStatement(CHECK_EMAIL_EXISTS_QUERY);
                    break;
                case PHONE_NUMBER:
                    preparedStatement = connection.prepareStatement(CHECK_PHONE_NUMBER_EXISTS_QUERY);
                    break;
            }

            preparedStatement.setString(1, value);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                pool.releaseConnection(connection, preparedStatement);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public boolean passwordMatches(ProxyConnection connection, String login, String password) throws DaoException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD_QUERY);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                pool.releaseConnection(connection, preparedStatement);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public User findUserById(String id) throws DaoException{
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_ID_QUERY);
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                String login = resultSet.getString(2);
                String password = resultSet.getString(3);
                String email = resultSet.getString(4);
                String phoneNumber = resultSet.getString(5);
                String userName = resultSet.getString(6);
                user.setLogin(login);
                user.setPassword(password);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                user.setUserName(userName);
            }

            return user;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by ID", e);
        } finally {

            try {
                pool.releaseConnection(connection, preparedStatement);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public User findUserByLoginAndPassword(String login, String password) throws DaoException{
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD_QUERY);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                String email = resultSet.getString(4);
                String phoneNumber = resultSet.getString(5);
                String userName = resultSet.getString(6);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                user.setUserName(userName);
            }

            return user;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by ID", e);
        } finally {

            try {
                pool.releaseConnection(connection, preparedStatement);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }
}
