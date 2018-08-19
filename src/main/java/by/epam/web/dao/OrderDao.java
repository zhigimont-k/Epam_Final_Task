package by.epam.web.dao;

import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface OrderDao extends AbstractDao<Order> {
    /**
     * Adds an order to database
     *
     * @param order order to be added
     *
     * @throws DaoException if SQLException occurs
     */
    void addOrder(Order order) throws DaoException;

    /**
     * Changes order's status
     *
     * @param orderId id of updated order
     * @param status  new status of order
     *
     * @throws DaoException if SQLException occurs
     */
    void changeOrderStatus(int orderId, String status) throws DaoException;

    /**
     * Sets order's status as 'cancelled' and returns money to the user's card if it was paid
     *
     * @param orderId ID of the order to cancel
     *
     * @throws DaoException if SQLException occurs
     */
    void cancelOrder(int orderId) throws DaoException;

    /**
     * Looks for order by id
     *
     * @param id id of order to look for
     *
     * @return Found order
     *
     * @throws DaoException if SQLException occurs
     */
    Optional<Order> findOrderById(int id) throws DaoException;

    /**
     * Returns list of all orders
     *
     * @param startPosition   position of the first element to return
     * @param numberOfRecords number of records to return
     *
     * @return List of found orders
     *
     * @throws DaoException if SQLException occurs
     */
    List<Order> findAllOrders(int startPosition, int numberOfRecords) throws DaoException;

    /**
     * Returns list of user's orders
     *
     * @param userId          id of the user whose orders to look for
     * @param startPosition   position of the first element to return
     * @param numberOfRecords number of records to return
     *
     * @return List of found orders
     *
     * @throws DaoException if SQLException occurs
     */
    List<Order> findOrdersByUser(int userId, int startPosition, int numberOfRecords)
            throws DaoException;


    Optional<Order> findOrderByUserAndTime(int userId, Timestamp timestamp)
            throws DaoException;

    /**
     * Returns list of activities of given order
     *
     * @param id id of the order whose activities t look for
     *
     * @return List of found orders
     *
     * @throws DaoException if SQLException occurs
     */
    List<Activity> findActivitiesByOrderId(int id) throws DaoException;

    /**
     * Returns list of emails of users who have confirmed upcoming orders in 1 day interval
     *
     * @return List of found emails
     *
     * @throws DaoException if SQLException occurs
     */
    List<String> findEmailsForUpcomingOrders() throws DaoException;

    /**
     * Sets order paid and subtracts order's price
     * from the money on the card of the user who made the order
     *
     * @param orderId id of the order to pay for
     *
     * @throws DaoException if SQLException occurs
     */
    void payForOrder(int orderId) throws DaoException;

    /**
     * Returns number of all orders
     *
     * @return number of orders
     *
     * @throws DaoException if SQLException occurs
     */
    int countOrders() throws DaoException;

    /**
     * Returns number of given user's orders
     *
     * @param userId id of the user to count orders of
     *
     * @return number of given user's orders
     *
     * @throws DaoException if SQLException occurs
     */
    int countUserOrders(int userId) throws DaoException;

    /**
     * Finds pending orders with expired time and sets their status to canceled
     */
    void cancelUnconfirmedOutdatedOrders() throws DaoException;
}
