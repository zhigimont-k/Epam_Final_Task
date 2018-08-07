package by.epam.web.dao.impl;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.OrderDao;
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
    private static Logger logger = LogManager.getLogger();

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_ORDER_ID_FIELD = "order_id";
    private static final String DB_ORDER_USER_ID_FIELD = "user_id";
    private static final String DB_ORDER_SERVICE_ID_FIELD = "service_id";
    private static final String DB_ORDER_STATUS_FIELD = "order_status";
    private static final String DB_ORDER_TIME_FIELD = "order_time";
    private static final String DB_ORDER_PRICE_FIELD = "order_price";
    private static final String DB_ORDER_PAID_FIELD = "paid";

    private static final String DB_ACTIVITY_ID_FIELD = "service_id";
    private static final String DB_ACTIVITY_NAME_FIELD = "service_name";
    private static final String DB_ACTIVITY_DESCRIPTION_FIELD = "service_description";
    private static final String DB_ACTIVITY_PRICE_FIELD = "service_price";
    private static final String DB_ACTIVITY_STATUS_FIELD = "service_status";

    private static final String DB_USER_EMAIL_FIELD = "user_email";

    private static final String INSERT_ORDER_INFO = "INSERT INTO order_info " +
            "(user_id, order_time) " +
            "VALUES (?, ?)";
    private static final String INSERT_ORDER_ACTIVITIES = "INSERT INTO order_link " +
            "(order_id, service_id) " +
            "VALUES (?, ?)";
    private static final String CANCEL_ORDER = "UPDATE order_info " +
            "SET order_status = 'cancelled', order_time = order_time WHERE order_id = ?";
    private static final String UPDATE_ORDER_STATUS = "UPDATE order_info " +
            "SET order_status = ?, order_time = order_time WHERE order_id = ?";
    private static final String FIND_ALL_ORDERS = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time, order_info.order_price, order_info.paid " +
            "FROM order_info " +
            "LIMIT ?,?";
    private static final String FIND_ORDER_BY_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time, order_info.order_price, order_info.paid " +
            "FROM order_info " +
            "WHERE order_info.order_id = ?";
    private static final String FIND_ORDERS_BY_USER_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time, order_info.order_price, order_info.paid " +
            "FROM order_info " +
            "WHERE order_info.user_id = ? ";
    private static final String FIND_ORDERS_BY_USER_ID_LIMITED = FIND_ORDERS_BY_USER_ID +
            "LIMIT ?,? ";
    private static final String FIND_ACTIVITIES_BY_ORDER_ID = "SELECT service.service_id, " +
            "service_name, service_description, service_status, service_price FROM service " +
            "JOIN order_link ON service.service_id = order_link.service_id " +
            "WHERE order_id = ?";
    private static final String FIND_EMAILS_FOR_UPCOMING_ORDERS =
            "SELECT DISTINCT user.user_email, order_info.order_id " +
                    "FROM user " +
                    "LEFT JOIN order_info " +
                    "ON order_info.user_id = user.user_id " +
                    "WHERE DATE(order_time) <= CURRENT_DATE + INTERVAL 1 DAY " +
                    "AND order_info.order_status = 'confirmed' AND order_info.reminded = 0";
    private static final String SET_ORDER_REMINDED = "UPDATE order_info " +
            "SET reminded = 1, order_time = order_time WHERE order_id = ?";
    private static final String SET_ORDER_PRICE = "UPDATE order_info " +
            "SET order_price = ?, order_time = order_time WHERE order_id = ?";
    private static final String RETURN_ORDER_MONEY = "UPDATE card " +
            "JOIN order_info " +
            "ON order_info.order_id = ? " +
            "SET paid = 0, money = money + order_price, order_info.order_time = order_info.order_time " +
            "WHERE card.user_id = order_info.user_id AND paid = 1";
    private static final String PAY_FOR_ORDER = "UPDATE card " +
            "JOIN order_info " +
            "ON order_info.order_id = ? " +
            "SET paid = 1, money = money - order_price, order_info.order_time = order_info.order_time  " +
            "WHERE card.user_id = order_info.user_id AND paid = 0";
    private static final String COUNT_ORDERS = "SELECT DISTINCT COUNT(*) FROM order_info";
    private static final String COUNT_USER_ORDERS = "SELECT DISTINCT COUNT(*) FROM order_info " +
            "WHERE user_id = ?";

    @Override
    public void addOrder(Order order) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);

            int userId = order.getUserId();
            Timestamp time = order.getDateTime();
            List<Activity> activityList = new ArrayList<>();
            for (int i = 0; i < order.activityListSize(); i++) {
                activityList.add(order.getActivity(i));
            }

            preparedStatement = connection.prepareStatement(INSERT_ORDER_INFO, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, time);

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int orderId = resultSet.getInt(1);
                order.setId(orderId);
            } else {
                throw new DaoException("Couldn't retrieve order's ID and status");
            }

            for (Activity activity : activityList) {
                preparedStatement = connection.prepareStatement(INSERT_ORDER_ACTIVITIES);

                preparedStatement.setInt(1, order.getId());
                preparedStatement.setInt(2, activity.getId());

                preparedStatement.executeUpdate();
            }

            preparedStatement = connection.prepareStatement(SET_ORDER_PRICE);
            preparedStatement.setBigDecimal(1, calculateOrderPrice(order.getActivityList()));
            preparedStatement.setInt(2, order.getId());
            preparedStatement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    logger.log(Level.INFO, "Encountered an error, made a rollback");
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Couldn't rollback connection: " + e.getMessage(), e);
            }
            throw new DaoException("Failed to add order" + e.getMessage(), e);
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
    public void changeOrderStatus(int id, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            if (Order.Status.CANCELLED.getName().equalsIgnoreCase(status)){
                preparedStatement = connection.prepareStatement(RETURN_ORDER_MONEY);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    logger.log(Level.INFO, "Encountered an error, made a rollback");
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Couldn't rollback connection: " + e.getMessage(), e);
            }
            throw new DaoException("Failed to change order status" + e.getMessage(), e);
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
    public List<Order> findAllOrders(int startPosition, int numberOfRecords) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_ALL_ORDERS);
            preparedStatement.setInt(1, startPosition);
            preparedStatement.setInt(2, numberOfRecords);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                Order order = new Order();
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);

                order.setId(currentOrderId);
                order.setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD));
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                order.setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders" + e.getMessage(), e);
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
    public Optional<Order> findOrderById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Order> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDER_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Order order = new Order();

                order.setId(id);
                order.setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD));
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                order.setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD));
                order.setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(id);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }

                result = Optional.of(order);
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by id" + e.getMessage(), e);
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
    public List<Order> findOrdersByUser(int userId, int startPosition, int numberOfRecords) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID_LIMITED);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, startPosition);
            preparedStatement.setInt(3, numberOfRecords);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);

                order.setId(currentOrderId);
                order.setUserId(userId);
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                order.setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD));
                order.setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders by user" + e.getMessage(), e);
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
    public List<Order> findOrdersByUser(int userId) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID);

            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);

                order.setId(currentOrderId);
                order.setUserId(userId);
                order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
                order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
                order.setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD));
                order.setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders by user" + e.getMessage(), e);
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
    public List<Order> findOrdersByUserAndStatus(User user, String status) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

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
                order.setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD));
                order.setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD));
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                for (Activity activity : activityList) {
                    order.addActivity(activity);
                }
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders by user and order status" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    private BigDecimal calculateOrderPrice(List<Activity> activityList) {
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
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_ACTIVITIES_BY_ORDER_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

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
            throw new DaoException("Failed to find activities by order id" + e.getMessage(), e);
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
    public List<String> findEmailsForUpcomingOrders() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        List<String> emailList = new ArrayList<>();
        List<Integer> orderIdList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_EMAILS_FOR_UPCOMING_ORDERS);

            while (resultSet.next()) {
                emailList.add(resultSet.getString(DB_USER_EMAIL_FIELD));
                orderIdList.add(resultSet.getInt(DB_ORDER_ID_FIELD));
            }
            preparedStatement = connection.prepareStatement(SET_ORDER_REMINDED);

            for (int orderId : orderIdList) {
                preparedStatement.setInt(1, orderId);
                preparedStatement.executeUpdate();
            }

            return emailList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find emails for upcoming orders" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(statement);
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }


    public void payForOrder(int orderId) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(PAY_FOR_ORDER);
            preparedStatement.setInt(1, orderId);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS);
            preparedStatement.setString(1, Order.Status.CONFIRMED.getName());
            preparedStatement.setInt(2, orderId);

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    logger.log(Level.INFO, "Encountered an error, made a rollback");
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Couldn't rollback connection: " + e.getMessage(), e);
            }
            throw new DaoException("Failed to pay for order: " + e.getMessage(), e);
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
    public int countOrders() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        int result;
        try {
            connection = pool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(COUNT_ORDERS);

            if (resultSet.next()) {
                result = resultSet.getInt(1);
            } else {
                throw new DaoException("Couldn't count orders");
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders" + e.getMessage(), e);
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
    public int countUserOrders(int userId) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        int result;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(COUNT_USER_ORDERS);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getInt(1);
            } else {
                throw new DaoException("Couldn't count user orders");
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    private Order buildOrder(int id, int userId, String status, Timestamp time, BigDecimal price,
                             boolean paid) {
//        order.setId(currentOrderId);
//        order.setUserId(userId);
//        order.setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD));
//        order.setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD));
//        order.setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD));
//        order.setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD));
        return new Order();
    }
}
