package by.epam.web.dao.order.impl;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.order.OrderDao;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.PoolException;
import by.epam.web.pool.ProxyConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_ORDER_ID_FIELD = "order_id";
    private static final String DB_ORDER_USER_ID_FIELD = "user_id";
    private static final String DB_ORDER_SERVICE_ID_FIELD = "service_id";
    private static final String DB_ORDER_STATUS_FIELD = "order_status";
    private static final String DB_ORDER_TIME_FIELD = "order_time";

    private static final String DB_ACTIVITY_ID_FIELD = "service_id";
    private static final String DB_ACTIVITY_NAME_FIELD = "service_name";
    private static final String DB_ACTIVITY_DESCRIPTION_FIELD = "service_description";
    private static final String DB_ACTIVITY_PRICE_FIELD = "service_price";
    private static final String DB_ACTIVITY_STATUS_FIELD = "service_status";

    private static final String INSERT_ORDER_INFO = "INSERT INTO order_info " +
            "(user_id, service_id, order_time) " +
            "VALUES (?, ?, ?)";
    private static final String INSERT_ORDER_ACTIVITIES = "INSERT INTO order_link " +
            "(order_id, service_id) " +
            "VALUES (?, ?)";
    private static final String CANCEL_ORDER = "UPDATE order_info " +
            "SET order_status = 'cancelled' WHERE order_id = ?";
    private static final String UPDATE_ORDER_STATUS = "UPDATE order_info " +
            "SET order_status = ? WHERE order_id = ?";
    private static final String FIND_ALL_ORDERS = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time " +
            "FROM order_info ";
    private static final String FIND_ORDER_BY_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time " +
            "FROM order_info " +
            "WHERE order_info.order_id = ?";
    private static final String FIND_ORDERS_BY_USER_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time " +
            "FROM order_info " +
            "WHERE order_info.user_id = ?";
    private static final String FIND_ORDERS_BY_USER_ID_AND_ORDER_STATUS =
            "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time " +
            "FROM order_info " +
            "WHERE order_info.user_id = ? AND order_info.order_status = ?";
    private static final String FIND_ACTIVITIES_BY_ORDER_ID = "SELECT service.service_id, " +
            "service_name, service_description, service_status, service_price FROM service " +
            "JOIN order_link ON service.service_id = order_link.service_id " +
            "WHERE order_id = ?";

    @Override
    public Order addOrder(Order order) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.getConnection();
            int userId = order.getUserId();
            Timestamp time = order.getDateTime();
            List<Activity> activityList = new ArrayList<>();
            for (int i = 0; i < order.activityListSize(); i++){
                activityList.add(order.getActivity(i));
            }

            preparedStatement = connection.prepareStatement(INSERT_ORDER_INFO, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, time);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int orderId = resultSet.getInt(DB_ORDER_ID_FIELD);
                order.setId(orderId);
                String orderStatus = resultSet.getString(DB_ORDER_STATUS_FIELD);
                order.setStatus(orderStatus);
            } else {
                throw new DaoException("Couldn't retrieve order's ID and status");
            }

            for (Activity activity : activityList){
                preparedStatement = connection.prepareStatement(INSERT_ORDER_ACTIVITIES);

                preparedStatement.setInt(1, order.getId());
                preparedStatement.setInt(2, activity.getId());
            }

            logger.log(Level.INFO, "Added order: " + order);

            return order;
        } catch (SQLException e) {
            throw new DaoException("Failed to add order", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public Optional<Order> changeOrderStatus(int id, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        Optional<Order> order;
        try {
            connection = pool.getConnection();

            order = findOrderById(id);

            if (order.isPresent()) {
                preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS);
                preparedStatement.setString(1, status);
                preparedStatement.setInt(2, id);
                logger.log(Level.INFO, preparedStatement);
                logger.log(Level.INFO, "Setting order's " + id + " status to " + status);
                preparedStatement.executeUpdate();
            } else {
                throw new DaoException("Couldn't find order by id: " + id);
            }

            return order;
        } catch (SQLException e) {
            throw new DaoException("Failed to change order status", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public Optional<Order> cancelOrder(int id) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        Optional<Order> order;
        try {
            connection = pool.getConnection();

            order = findOrderById(id);

            if (order.isPresent()) {
                preparedStatement = connection.prepareStatement(CANCEL_ORDER);
                preparedStatement.setInt(1, id);
                logger.log(Level.INFO, preparedStatement);
                logger.log(Level.INFO, "Cancelling order " + id);
                preparedStatement.executeUpdate();
            } else {
                throw new DaoException("Couldn't find order by id: " + id);
            }

            return order;
        } catch (SQLException e) {
            throw new DaoException("Failed to cancel order", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public List<Order> findAllOrders() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ALL_ORDERS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);

                order.setId(currentOrderId);
                order.setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD));
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                for (Activity activity : activityList){
                    order.addActivity(activity);
                }
                logger.log(Level.INFO, "Found order: " + order);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }


    @Override
    public Optional<Order> findOrderById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Order> result = Optional.empty();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDER_BY_ID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Order order = new Order();

                order.setId(id);
                order.setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD));
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(id);
                for (Activity activity : activityList){
                    order.addActivity(activity);
                }
                logger.log(Level.INFO, "Found order by id: " + order);
                result = Optional.of(order);
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by id", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public List<Order> findOrdersByUser(User user) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID);

            int userId = user.getId();
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);

                order.setId(currentOrderId);
                order.setUserId(userId);
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }
                logger.log(Level.INFO, "Found order: " + order);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders by user", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public List<Order> findOrdersByUserAndStatus(User user, String status) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID);

            int userId = user.getId();
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(1, status);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);

                order.setId(currentOrderId);
                order.setUserId(userId);
                order.setStatus(status);
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }
                logger.log(Level.INFO, "Found order: " + order);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders by user and order status", e);
        } finally {
            try {
                pool.releaseConnection(connection);
                closeStatement(preparedStatement);
            } catch (PoolException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public BigDecimal calculateOrderPrice(int orderId) throws DaoException {
        List<Activity> activityList = findActivitiesByOrderId(orderId);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Activity activity : activityList) {
            totalPrice = totalPrice.add(activity.getPrice());
        }
        return totalPrice;
    }

    @Override
    public List<Activity> findActivitiesByOrderId(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Activity> activityList = new ArrayList<>();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ACTIVITIES_BY_ORDER_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Activity activity = new Activity();

                activity.setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD));
                activity.setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD));
                activity.setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD));
                activity.setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD));
                activity.setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD));

                activityList.add(activity);
            }

            return activityList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find activities by order id", e);
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
