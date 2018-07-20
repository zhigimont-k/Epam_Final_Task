package by.epam.web.dao.order;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDao extends AbstractDao<Order> {
    Order addOrder(Order order) throws DaoException;
    Order cancelOrder(int orderId) throws DaoException;
    Order changeOrderStatus(int orderId, String status) throws DaoException;
    Order findOrderById(int id) throws DaoException;
    List<Order> findAllOrders() throws DaoException;
    List<Order> findOrdersByUser(User user) throws DaoException;
    List<Order> findOrdersByUserAndStatus(User user, String status) throws DaoException;
    BigDecimal calculateOrderPrice(int orderId) throws DaoException;
}
