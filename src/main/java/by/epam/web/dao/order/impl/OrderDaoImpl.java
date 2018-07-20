package by.epam.web.dao.order.impl;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.order.OrderDao;
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
import java.util.LinkedList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_ORDER_ID_FIELD = "order_id";
    private static final String DB_ORDER_USER_ID_FIELD = "user_id";
    private static final String DB_ORDER_SERVICE_ID_FIELD = "service_id";
    private static final String DB_ORDER_STATUS_FIELD = "order_status";
    private static final String DB_ORDER_TIME_FIELD = "order_time";

    private static final String INSERT_ORDER = "INSERT INTO order_info " +
            "(user_id, service_id, order_time) " +
            "VALUES (?, ?, ?)";
    private static final String CANCEL_ORDER = "UPDATE order_info " +
            "SET order_status = 'cancelled' WHERE order_id = ?";
    private static final String UPDATE_ORDER_STATUS = "UPDATE service " +
            "SET order_status = ? WHERE order_id = ?";
    private static final String FIND_ALL_ORDERS = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.service_id, order_info.order_status, order_info.order_time " +
            "FROM order_info ";
    private static final String FIND_ORDERS_BY_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.service_id, order_info.order_status, order_info.order_time " +
            "FROM order_info " +
            "WHERE order_info.order_id = ?";
    private static final String FIND_ORDERS_BY_USER_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.service_id, order_info.order_status, order_info.order_time " +
            "FROM order_info " +
            "WHERE order_info.user_id = ?";

    @Override
    public Order addOrder(Order order) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.getConnection();
            int userId = order.getUserId();
            Timestamp time = order.getDateTime();

            for (int i = 0; i < order.size(); i++){
                int activityId = order.get(i);
            }

            preparedStatement = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, activityId);
            preparedStatement.setTimestamp(3, time);

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
    public Order changeOrderStatus(int id, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        Order order;
        try {
            connection = pool.getConnection();

            order = findOrderById(id);

            if (order != null) {
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
    public Order cancelOrder(int id) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        Order order;
        try {
            connection = pool.getConnection();

            order = findOrderById(id);

            if (order != null) {
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
        List<Order> orderList = new LinkedList<>();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ALL_ORDERS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (orderList.isEmpty() || orderList.get(orderList.size()-1).getId() !=
                        resultSet.getInt(DB_ORDER_ID_FIELD)){
                    Order order = new Order();
                    order.setId(resultSet.getInt(DB_ORDER_ID_FIELD));
                    order.setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD));
                    order.add(resultSet.getInt(DB_ORDER_SERVICE_ID_FIELD));
                    order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                    order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                    logger.log(Level.INFO, "Found order: " + order);
                    orderList.add(order);
                } else {
                    Order lastOrderInList = orderList.get(orderList.size()-1);
                    lastOrderInList.add(resultSet.getInt(DB_ORDER_SERVICE_ID_FIELD));
                    logger.log(Level.INFO, "Added activity to order: " + lastOrderInList);
                }

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
    public Order findOrderById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Order order = null;
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDERS_BY_ID);
            preparedStatement.setInt(1, (int) id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                order = new Order();

                order.setId(id);
                order.setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD));
                order.add(resultSet.getInt(DB_ORDER_SERVICE_ID_FIELD));
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
            }

            return order;
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
    public List<Order> findOrdersByUser(User user) throws DaoException{
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new LinkedList<>();
        int userId = user.getId();
        try {
            connection = pool.getConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Order order = new Order();

                order.setId(resultSet.getInt(DB_ORDER_ID_FIELD));
                order.setUserId(userId);
                order.setActivityId(resultSet.getInt(DB_ORDER_SERVICE_ID_FIELD));
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));

                orderList.add(order);
            }

            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by user", e);
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
    public List<Order> findOrdersByUserAndStatus(User user, String status) {
        return null;
    }

    @Override
    public BigDecimal calculateOrderPrice(int orderId) {
        return null;
    }
}
