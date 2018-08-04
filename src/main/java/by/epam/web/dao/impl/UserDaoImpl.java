package by.epam.web.dao.impl;

import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.PoolException;
import by.epam.web.pool.ProxyConnection;
import by.epam.web.dao.DaoException;
import by.epam.web.dao.UserDao;
import by.epam.web.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static Logger logger = LogManager.getLogger();

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_USER_ID_FIELD = "user_id";
    private static final String DB_LOGIN_FIELD = "login";
    private static final String DB_PASSWORD_FIELD = "password";
    private static final String DB_USER_NAME_FIELD = "user_name";
    private static final String DB_USER_EMAIL_FIELD = "user_email";
    private static final String DB_PHONE_NUMBER_FIELD = "phone_number";
    private static final String DB_USER_STATUS_FIELD = "user_status";

    private static final String DB_CARD_NUMBER_FIELD = "card_number";

    private static final String INSERT_USER = "INSERT INTO user " +
            "(login, password, user_email, phone_number, user_name) " +
            "VALUES (?, SHA1(?), ?, ?, ?)";
    private static final String FIND_USER_BY_ID = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status, card.card_number " +
            "FROM user, card " +
            "WHERE user.user_id = ? AND card.user_id = user.user_id";
    private static final String FIND_USER_BY_LOGIN = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status, card_number " +
            "FROM user " +
            "JOIN card " +
            "ON user.user_id = card.user_id " +
            "WHERE user.login = ?";
    private static final String FIND_USER_BY_EMAIL = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user " +
            "WHERE user.user_email = ?";
    private static final String FIND_USER_BY_PHONE_NUMBER = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user " +
            "WHERE user.phone_number = ?";
    private static final String FIND_USER_BY_LOGIN_AND_PASSWORD = FIND_USER_BY_LOGIN +
            " AND user.password = SHA1(?)";
    private static final String FIND_ALL_USERS = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user ";
    private static final String UPDATE_USER_STATUS = "UPDATE user " +
            "SET user_status = ? WHERE user_id = ?";
    private static final String UPDATE_USER = "UPDATE user " +
            "SET password = SHA1(?), user_name = ? " +
            "WHERE user_id = ?";
    private static final String UPDATE_USER_NAME = "UPDATE user " +
            "SET user_name = ? " +
            "WHERE user_id = ?";
    private static final String ADD_MONEY_TO_CARD = "UPDATE card " +
            "SET money = money + ? " +
            "WHERE card_number = ?";
    private static final String ADD_CARD = "INSERT INTO card " +
            "(card_number, user_id) " +
            "VALUES (?, ?)";
    private static final String FIND_USER_BY_CARD_NUMBER = "SELECT card_number, money, user_id " +
            "FROM card " +
            "WHERE card_number = ?";
    private static final String FIND_MONEY_ON_CARD_BY_CARD_NUMBER = "SELECT money " +
            "FROM card " +
            "WHERE card_number = ?";
    private static final String FIND_USER_BY_ID_AND_CARD_NUMBER = "SELECT user.user_id, " +
            "user.login, user.password, user.user_email, user.phone_number, user.user_name, user.user_status " +
            "FROM user, card " +
            "WHERE user.user_id = ? AND card.card_number = ?";

    @Override
    public User register(User user) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            String login = user.getLogin();
            String password = user.getPassword();
            String email = user.getEmail();
            String phoneNumber = user.getPhoneNumber();
            String userName = user.getUserName();
            String cardNumber = user.getCardNumber();

            preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, userName);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int userId = resultSet.getInt(1);
                user.setId(userId);
                Optional<User> added = findUserById(userId);
                if (added.isPresent()) {
                    user.setStatus(added.get().getStatus());
                }
            } else {
                throw new DaoException("Couldn't retrieve user's ID and status");
            }

            preparedStatement = connection.prepareStatement(ADD_CARD);
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();

            return user;
        } catch (SQLException e) {
            throw new DaoException("Failed to register user" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean propertyExists(UniqueUserInfo property, String value) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            connection = pool.takeConnection();
            switch (property) {
                case LOGIN:
                    preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN);
                    break;
                case EMAIL:
                    preparedStatement = connection.prepareStatement(FIND_USER_BY_EMAIL);
                    break;
                case PHONE_NUMBER:
                    preparedStatement = connection.prepareStatement(FIND_USER_BY_PHONE_NUMBER);
                    break;
                case CARD_NUMBER:
                    preparedStatement = connection.prepareStatement(FIND_USER_BY_CARD_NUMBER);
                    break;
            }

            preparedStatement.setString(1, value);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<User> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(login);
                user.setPassword(password);
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
                user.setCardNumber(resultSet.getString(DB_CARD_NUMBER_FIELD));
                result = Optional.of(user);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login and password: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<User> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(login);
                user.setPassword(resultSet.getString(DB_PASSWORD_FIELD));
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
                result = Optional.of(user);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public Optional<User> findUserById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<User> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();

                user.setId(id);
                user.setLogin(resultSet.getString(DB_LOGIN_FIELD));
                user.setPassword(resultSet.getString(DB_PASSWORD_FIELD));
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
                result = Optional.of(user);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by id: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public List<User> findAllUsers() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        List<User> userList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_USERS);

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(resultSet.getString(DB_LOGIN_FIELD));
                user.setPassword(resultSet.getString(DB_PASSWORD_FIELD));
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find users" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(statement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public void changeUserStatus(int userId, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(UPDATE_USER_STATUS);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to change user status: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public void updateUser(int id, String password, String userName) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, userName);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to update user: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public void updateUserName(int id, String userName) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(UPDATE_USER_NAME);
            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to change user name: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<User> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(resultSet.getString(DB_LOGIN_FIELD));
                user.setPassword(resultSet.getString(DB_PASSWORD_FIELD));
                user.setEmail(email);
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
                result = Optional.of(user);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by email: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    public void addMoneyToCard(String cardNumber, BigDecimal amount) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(ADD_MONEY_TO_CARD);
            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to add money to card: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public BigDecimal findMoneyByCardNumber(String cardNumber) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        BigDecimal money = BigDecimal.ZERO;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_MONEY_ON_CARD_BY_CARD_NUMBER);
            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                money = money.add(resultSet.getBigDecimal("money"));
            }
            return money;

        } catch (SQLException e) {
            throw new DaoException("Failed to find money: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public Optional<User> findUserByIdAndCardNumber(int userId, String cardNumber)
            throws DaoException{
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<User> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_USER_BY_ID_AND_CARD_NUMBER);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, cardNumber);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(DB_USER_ID_FIELD));
                user.setLogin(resultSet.getString(DB_LOGIN_FIELD));
                user.setPassword(DB_PASSWORD_FIELD);
                user.setEmail(resultSet.getString(DB_USER_EMAIL_FIELD));
                user.setPhoneNumber(resultSet.getString(DB_PHONE_NUMBER_FIELD));
                user.setUserName(resultSet.getString(DB_USER_NAME_FIELD));
                user.setStatus(resultSet.getString(DB_USER_STATUS_FIELD));
                result = Optional.of(user);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by login and password: " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }
}
