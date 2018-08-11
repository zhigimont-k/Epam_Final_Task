package by.epam.web.dao;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderDao extends AbstractDao<Order> {
    void addOrder(Order order) throws DaoException;
    void changeOrderStatus(int orderId, String status) throws DaoException;
    Optional<Order> findOrderById(int id) throws DaoException;
    List<Order> findAllOrders(int startPosition, int numberOfRecords) throws DaoException;
    List<Order> findOrdersByUser(int userId, int startPosition, int numberOfRecords) throws DaoException;
    List<Order> findOrdersByUser(int userId) throws DaoException;
    List<Activity> findActivitiesByOrderId(int id) throws DaoException;
    List<String> findEmailsForUpcomingOrders() throws DaoException;
    void payForOrder(int orderId) throws DaoException;
    int countOrders() throws DaoException;
    int countUserOrders(int userId) throws DaoException;
    void cancelUnconfirmedOutdatedOrders();
}
