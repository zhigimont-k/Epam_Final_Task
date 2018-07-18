package by.epam.web.dao.activity.impl;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.activity.ActivityDao;
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
import java.util.LinkedList;
import java.util.List;

public class ActivityDaoImpl implements ActivityDao {
    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_ACTIVITY_ID_FIELD = "service_id";
    private static final String DB_ACTIVITY_NAME_FIELD = "service_name";
    private static final String DB_ACTIVITY_DESCRIPTION_FIELD = "service_description";
    private static final String DB_ACTIVITY_PRICE_FIELD = "service_price";
    private static final String DB_ACTIVITY_STATUS_FIELD = "service_status";

    private static final String INSERT_ACTIVITY = "INSERT INTO service " +
            "(service_name, service_description, service_price, service_status) " +
            "VALUES (?, SHA1(?), ?, ?, ?)";
    private static final String FIND_ACTIVITY_BY_NAME_QUERY = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_name = ?";
    private static final String FIND_ACTIVITY_BY_ID_QUERY = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_id = ?";
    private static final String FIND_ALL_ACTIVITIES = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service ";
    private static final String UPDATE_ACTIVITY_STATUS = "UPDATE service " +
            "SET service_status = ? WHERE service_id = ?";

    public Activity insertActivity(Activity activity) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.getConnection();
            String name = activity.getName();
            String description = activity.getDescription();
            BigDecimal price = activity.getPrice();

            preparedStatement = connection.prepareStatement(INSERT_ACTIVITY, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setBigDecimal(3, price);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int activityId = resultSet.getInt(DB_ACTIVITY_ID_FIELD);
                activity.setId(activityId);
                String activityStatus = resultSet.getString(DB_ACTIVITY_STATUS_FIELD);
                activity.setStatus(activityStatus);
            } else {
                throw new DaoException("Couldn't retrieve activity's ID and status");
            }

            logger.log(Level.INFO, "Added activity: " + activity);

            return activity;
        } catch (SQLException e) {
            throw new DaoException("Failed to add activity", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public Activity findActivityById(int id) throws DaoException{
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Activity activity = null;
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ACTIVITY_BY_ID_QUERY);
            preparedStatement.setInt(1, (int) id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                activity = new Activity();

                activity.setId(id);
                activity.setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD));
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD));
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));
            }

            return activity;
        } catch (SQLException e) {
            throw new DaoException("Failed to find activity by id", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }


    public Activity findActivityByName(String name) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Activity activity = null;
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ACTIVITY_BY_NAME_QUERY);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                activity = new Activity();

                activity.setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD));
                activity.setName(name);
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD));
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));
            }

            return activity;
        } catch (SQLException e) {
            throw new DaoException("Failed to find activity by name", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public List<Activity> findAllActivities() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Activity> activityList = new LinkedList<>();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ALL_ACTIVITIES);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Activity activity = new Activity();

                activity.setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD));
                activity.setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD));
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD));
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));
                logger.log(Level.INFO, "Found activity: " + activity);
                activityList.add(activity);
            }
            return activityList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find activities", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    public Activity changeActivityStatus(int id, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        Activity activity;
        try {
            connection = pool.getConnection();

            activity = findActivityById(id);

            if (activity != null) {
                preparedStatement = connection.prepareStatement(UPDATE_ACTIVITY_STATUS);
                preparedStatement.setString(1, status);
                preparedStatement.setInt(2, id);
                logger.log(Level.INFO, preparedStatement);
                logger.log(Level.INFO, "Setting activity's " + id + " status to " + status);
                preparedStatement.executeUpdate();
            } else {
                throw new DaoException("Couldn't find activity by id: " + id);
            }

            return activity;
        } catch (SQLException e) {
            throw new DaoException("Failed to change activity status", e);
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
