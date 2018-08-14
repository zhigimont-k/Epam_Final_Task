package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.OrderDao;
import by.epam.web.dao.UserDao;
import by.epam.web.dao.impl.OrderDaoImpl;
import by.epam.web.dao.impl.UserDaoImpl;
import by.epam.web.entity.Activity;
import by.epam.web.entity.Order;
import by.epam.web.entity.User;
import by.epam.web.validation.NumberValidator;
import by.epam.web.validation.OrderValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private static Logger logger = LogManager.getLogger();
    private static final OrderDao orderDao = new OrderDaoImpl();

    OrderService() {
    }

    public boolean addOrder(String userId, Timestamp time, List<Activity> activityList)
            throws ServiceException {
        if (!NumberValidator.getInstance().validateId(userId) ||
                !OrderValidator.getInstance().validateOrderTimeAfterNow(time) ||
                activityList.isEmpty()) {
            return false;
        }
        Order order;
        try {
            order = new Order();
            order.setUserId(Integer.parseInt(userId));
            order.setDateTime(time);
            for (Activity activity : activityList) {
                order.addActivity(activity);
            }
            orderDao.addOrder(order);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Order> findOrderById(String id) throws ServiceException {
        try {
            return (NumberValidator.getInstance().validateId(id)) ?
                    orderDao.findOrderById(Integer.parseInt(id)) :
                    Optional.empty();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findOrdersByUser(int userId, int startPosition, int numberOfRecords)
            throws ServiceException {
        try {
            return orderDao.findOrdersByUser(userId, startPosition, numberOfRecords);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean orderExists(int userId, Timestamp timestamp) throws ServiceException {
        try {
            Optional<Order> found = orderDao.findOrderByUserAndTime(userId, timestamp);
            if (found.isPresent()) {
                logger.log(Level.INFO, "order: "+found.get());
                return !Order.Status.CANCELLED.getName().equalsIgnoreCase(found.get().getStatus());
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findAllOrders(int startPosition, int numberOfRecords) throws ServiceException {
        try {
            return orderDao.findAllOrders(startPosition, numberOfRecords);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void changeOrderStatus(int id, String status) throws ServiceException {
        try {
            orderDao.changeOrderStatus(id, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void cancelOrder(int id) throws ServiceException {
        try {
            Optional<Order> orderOptional = orderDao.findOrderById(id);
            if (orderOptional.isPresent() &&
                    OrderValidator.getInstance().validateOrderTimeAfterNow
                            (orderOptional.get().getDateTime())) {
                orderDao.cancelOrder(id);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

    }

    public void payForOrder(int orderId) throws ServiceException {
        try {
            Optional<Order> orderOptional = orderDao.findOrderById(orderId);
            if (orderOptional.isPresent() &&
                    OrderValidator.getInstance().validateOrderTimeAfterNow
                            (orderOptional.get().getDateTime())) {
                orderDao.payForOrder(orderId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public int countOrders() throws ServiceException {
        try {
            return orderDao.countOrders();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public int countUserOrders(int userId) throws ServiceException {
        try {
            return orderDao.countUserOrders(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
