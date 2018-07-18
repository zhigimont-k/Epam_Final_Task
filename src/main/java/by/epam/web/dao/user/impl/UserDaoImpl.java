package by.epam.web.dao.user.impl;

import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.PoolException;
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
import java.util.LinkedList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();

    public enum UniqueUserInfo {
        LOGIN, EMAIL, PHONE_NUMBER
    }

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_USER_ID_FIELD = "user_id";
    private static final String DB_LOGIN_FIELD = "login";
    private static final String DB_PASSWORD_FIELD = "password";
    private static final String DB_USER_NAME_FIELD = "user_name";
    private static final String DB_USER_EMAIL_FIELD = "user_email";
    private static final String DB_PHONE_NUMBER_FIELD = "phone_number";
    private static final String DB_USER_STATUS_FIELD = "user_status";

    private static final String INSERT_USER = "INSERT INTO user " +
            "(login, password, user_email, phone_number, user_name) " +
            "VALUES (?, SHA1(?), ?, ?, ?)";
    private static final String FIND_USER_BY_LOGIN_QUERY = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user " +
            "WHERE user.login = ?";
    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user " +
            "WHERE user.user_email = ?";
    private static final String FIND_USER_BY_PHONE_NUMBER = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user " +
            "WHERE user.phone_number = ?";
    private static final String FIND_USER_BY_LOGIN_AND_PASSWORD_QUERY = FIND_USER_BY_LOGIN_QUERY +
            " AND user.password = SHA1(?)";
    private static final String FIND_ALL_USERS = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user ";
    private static final String UPDATE_USER_STATUS = "UPDATE user " +
            "SET user_status = ? WHERE login = ?";

    public User addUser(User user) throws DaoException {
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

            preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, userName);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int userID = resultSet.getInt(DB_USER_ID_FIELD);
                user.setId(userID);
                String userStatus = resultSet.getString(DB_USER_STATUS_FIELD);
                user.setStatus(userStatus);
            } else {
                throw new DaoException("Couldn't retrieve user's ID and status");
            }

            logger.log(Level.INFO, "Registered user: " + user);

            return user;
        } catch (SQLException e) {
            throw new DaoException("Failed to addUser user", e);
        } finally {

            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
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
            switch (property) {
                case LOGIN:
                    preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_QUERY);
                    break;
                case EMAIL:
                    preparedStatement = connection.prepareStatement(FIND_USER_BY_EMAIL_QUERY);
                    break;
                case PHONE_NUMBER:
                    preparedStatement = connection.prepareStatement(FIND_USER_BY_PHONE_NUMBER);
                    break;
            }

            preparedStatement.setString(1, value);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public User findUserByLoginAndPassword(String login, String password) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD_QUERY);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(login);
                user.setPassword(password);
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
            }

            return user;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login and password", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public User findUserByLogin(String login) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_QUERY);
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();

                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(login);
                user.setPassword(resultSet.getString(DB_PASSWORD_FIELD));
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
            }

            return user;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login and password", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public List<User> findAllUsers() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<User> userList = new LinkedList<>();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ALL_USERS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(resultSet.getString(DB_LOGIN_FIELD));
                user.setPassword(resultSet.getString(DB_PASSWORD_FIELD));
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
                logger.log(Level.INFO, "Found user: " + user);
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find users", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public User changeUserStatus(String login, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        User user;
        try {
            connection = pool.getConnection();

            user = findUserByLogin(login);

            if (user != null) {
                preparedStatement = connection.prepareStatement(UPDATE_USER_STATUS);
                preparedStatement.setString(1, status);
                preparedStatement.setString(2, login);
                logger.log(Level.INFO, preparedStatement);
                logger.log(Level.INFO, "Setting user's " + login + " status to " + status);
                preparedStatement.executeUpdate();
            } else {
                throw new DaoException("Couldn't find user by login: " + login);
            }

            return user;
        } catch (SQLException e) {
            throw new DaoException("Failed to change user status", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }
}
