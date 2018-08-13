package by.epam.web.controller.listener;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.OrderDao;
import by.epam.web.dao.impl.OrderDaoImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class OrderStatusUpdaterThread implements Runnable {
    private static Logger logger = LogManager.getLogger();
    private static final OrderDao orderDao = new OrderDaoImpl();

    @Override
    public void run() {
        logger.log(Level.INFO, "Cancelling unconfirmed outdated orders...");
        try {
            orderDao.cancelUnconfirmedOutdatedOrders();
        } catch (DaoException e){
            logger.log(Level.ERROR, "Couldn't cancel outdated orders: " + e.getMessage());
        }
    }
}