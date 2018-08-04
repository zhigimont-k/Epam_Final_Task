package by.epam.web.dao.impl;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.ActivityDao;
import by.epam.web.entity.Activity;
import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.PoolException;
import by.epam.web.pool.ProxyConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ActivityDaoImpl implements ActivityDao {
    private static Logger logger = LogManager.getLogger();

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_ACTIVITY_ID_FIELD = "service_id";
    private static final String DB_ACTIVITY_NAME_FIELD = "service_name";
    private static final String DB_ACTIVITY_DESCRIPTION_FIELD = "service_description";
    private static final String DB_ACTIVITY_PRICE_FIELD = "service_price";
    private static final String DB_ACTIVITY_STATUS_FIELD = "service_status";

    private static final String INSERT_ACTIVITY = "INSERT INTO service " +
            "(service_name, service_description, service_price) " +
            "VALUES (?, ?, ?)";
    private static final String FIND_ACTIVITY_BY_NAME = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_name = ?";
    private static final String FIND_ACTIVITY_BY_ID = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_id = ?";
    private static final String FIND_ALL_ACTIVITIES = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service ";
    private static final String FIND_AVAILABLE_ACTIVITIES = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_status = 'available'";
    private static final String UPDATE_ACTIVITY_STATUS = "UPDATE service " +
            "SET service_status = ? WHERE service_id = ?";
    private static final String UPDATE_ACTIVITY = "UPDATE service " +
            "SET service_name = ?, service_description = ?, service_price = ?, service_status = ? " +
            "WHERE service_id = ?";

    @Override
    public void addActivity(Activity activity) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            String name = activity.getName();
            String description = activity.getDescription();
            BigDecimal price = activity.getPrice();

            preparedStatement = connection.prepareStatement(INSERT_ACTIVITY);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setBigDecimal(3, price);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to add activity: " + e.getMessage(), e);
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
    public Optional<Activity> findActivityById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Activity> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_ACTIVITY_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Activity activity = new Activity();

                activity.setId(id);
                activity.setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD));
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD).trim());
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));
                result = Optional.of(activity);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find activity by id" + e.getMessage(), e);
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
    public Optional<Activity> findActivityByName(String name) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Activity> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_ACTIVITY_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Activity activity = new Activity();

                activity.setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD));
                activity.setName(name);
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD).trim());
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));
                result = Optional.of(activity);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find activity by name" + e.getMessage(), e);
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
    public List<Activity> findAllActivities() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        List<Activity> activityList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_ACTIVITIES);

            while (resultSet.next()) {
                Activity activity = new Activity();

                activity.setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD));
                activity.setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD));
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD).trim());
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));
                activityList.add(activity);
            }
            return activityList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find activities" + e.getMessage(), e);
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
    public void changeActivityStatus(int id, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(UPDATE_ACTIVITY_STATUS);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to change activity status" + e.getMessage(), e);
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
    public List<Activity> findAvailableActivities() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        List<Activity> activityList = new LinkedList<>();
        try {
            connection = pool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_AVAILABLE_ACTIVITIES);

            while (resultSet.next()) {
                Activity activity = new Activity();

                activity.setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD));
                activity.setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD));
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD).trim());
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));
                activityList.add(activity);
            }
            return activityList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find available activities" + e.getMessage(), e);
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
    public void updateActivity(int id, String name, String description,
                               BigDecimal price, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(UPDATE_ACTIVITY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setBigDecimal(3, price);
            preparedStatement.setString(4, status);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to update activity:  " + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    Activity buildActivity(int id, String name, String description, String status,
                           BigDecimal price){
        return new Activity();
    }
}