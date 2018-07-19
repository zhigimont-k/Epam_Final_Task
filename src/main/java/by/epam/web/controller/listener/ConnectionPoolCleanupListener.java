package by.epam.web.controller.listener;

import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.PoolException;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConnectionPoolCleanupListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            ConnectionPool.getInstance().closeConnectionPool();
        } catch (PoolException e){
            logger.fatal("Can't close connection pool", e);
            throw new RuntimeException("Can't close connection pool", e);
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
    }

}
