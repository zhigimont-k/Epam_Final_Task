package by.epam.web.controller.listener;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.OrderDao;
import by.epam.web.dao.impl.OrderDaoImpl;
import by.epam.web.dao.UserDao;
import by.epam.web.dao.impl.UserDaoImpl;
import by.epam.web.entity.User;
import by.epam.web.pool.ConnectionPool;
import by.epam.web.util.mail.MailComposer;
import by.epam.web.util.mail.MailSenderThread;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class OrderReminderThread implements Runnable {
    private static Logger logger = LogManager.getLogger();
    private static final OrderDao orderDao = new OrderDaoImpl();
    private static final UserDao userDao = new UserDaoImpl();

    @Override
    public void run() {
        try {
            List<String> emailList = orderDao.findEmailsForUpcomingOrders();
            List<String> userLoginList = new ArrayList<>();
            for (String email : emailList) {
                Optional<User> userOptional = userDao.findUserByEmail(email);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();

                    userLoginList.add((user.getUserName() != null && !user.getUserName().isEmpty()) ?
                            user.getUserName() : user.getLogin());
                }
            }
            for (int i = 0; i < emailList.size(); i++) {
                new MailSenderThread(emailList.get(i), MailComposer.getOrderReminderMessageTheme(),
                        MailComposer.getOrderReminderMessage(userLoginList.get(i))).start();
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Couldn't retrieve email list for upcoming orders: " + e);
        }
    }
}
