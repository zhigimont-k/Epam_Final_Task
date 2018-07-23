package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.order.OrderDao;
import by.epam.web.dao.order.impl.OrderDaoImpl;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private static final OrderDao orderDao = new OrderDaoImpl();

    OrderService() {
    }

    public Order addOrder(int userId, Timestamp time, List<Activity> activityList) throws ServiceException {
        Order order;
        try {
            order = new Order();
            order.setUserId(userId);
            order.setDateTime(time);
            for (Activity activity : activityList) {
                order.addActivity(activity);
            }
            return orderDao.addOrder(order);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Order> findOrderById(int id) throws ServiceException {
        try {
            return orderDao.findOrderById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findOrdersByUser(User user) throws ServiceException {
        try {
            return orderDao.findOrdersByUser(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findOrdersByUserAndStatus(User user, String status) throws ServiceException {
        try {
            return orderDao.findOrdersByUserAndStatus(user, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findAllOrders() throws ServiceException {
        try {
            return orderDao.findAllOrders();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Order> changeOrderStatus(int id, String status) throws ServiceException {
        try {
            return orderDao.changeOrderStatus(id, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
