package by.epam.web.dao.user.impl;

import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.ConnectionPoolException;
import by.epam.web.pool.ProxyConnection;
import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.UserDao;
import by.epam.web.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDaoImpl implements UserDao {
    private final ConnectionPool pool = ConnectionPool.getInstance();

    private static final String REGISTER_USER = "INSERT INTO user " +
            "(login, password, user_email, phone_number, user_name) " +
            "VALUES (?, SHA1(?), ?, ?, ?)";
    private static final String CHECK_USER_EXISTS = "SELECT user.user_id FROM user WHERE user.login = ?";
    private static final String CHECK_PASSWORD_MATCH = "SELECT user.user_id FROM user WHERE user.login = ? AND user.password = SHA1(?)";


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
//
//            if (userExists(connection, login)) {
//                throw new UserAlreadyExistsException();
//            }
            preparedStatement = connection.prepareStatement(REGISTER_USER, Statement.RETURN_GENERATED_KEYS);


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

    public boolean login(User user) throws DaoException {
        ProxyConnection connection = null;
        try {
            connection = pool.getConnection();
            String login = user.getLogin();
            String password = user.getPassword();

//            if (userExists(connection, login)) {
//                throw new NoSuchUserException();
//            }
//
//            if (!passwordMatches(connection, login, password)) {
//                throw new IncorrectPasswordException();
//            }
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

    public boolean userExists(ProxyConnection connection, String login) throws DaoException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(CHECK_USER_EXISTS);
            preparedStatement.setString(1, login);

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
            preparedStatement = connection.prepareStatement(CHECK_PASSWORD_MATCH);
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
}
