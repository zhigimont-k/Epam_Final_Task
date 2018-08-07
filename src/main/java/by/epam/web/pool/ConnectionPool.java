package by.epam.web.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool instance;
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static ReentrantLock lock = new ReentrantLock();

    private BlockingQueue<ProxyConnection> availableConnections;
    private Deque<ProxyConnection> unavailableConnections;

    private ConnectionPool() {
        ConnectionManager.getInstance().buildPool();
        initPool();
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    isCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private void initPool() {
        availableConnections = new LinkedBlockingQueue<>();
        unavailableConnections = new ArrayDeque<>();
        for (int i = 0; i < ConnectionManager.getInstance().getPoolSize(); i++) {
            try {
                createConnection();
            } catch (SQLException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }

        if (availableConnections.size() < ConnectionManager.INITIAL_POOL_SIZE) {
            for (int i = availableConnections.size() - 1;
                 i < ConnectionManager.getInstance().getPoolSize(); i++) {
                try {
                    createConnection();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
        }

        if (availableConnections.isEmpty()) {
            logger.fatal("Couldn't create any connections");
            throw new RuntimeException("Couldn't create any connections");
        } else if (availableConnections.size() == ConnectionManager.INITIAL_POOL_SIZE) {
            logger.log(Level.INFO, "Successfully initialized connection pool");
        }

    }

    public ProxyConnection takeConnection() throws PoolException {
        ProxyConnection connection = null;
        if (availableConnections.isEmpty() &&
                unavailableConnections.size() >= ConnectionManager.INITIAL_POOL_SIZE &&
                unavailableConnections.size() < ConnectionManager.MAX_POOL_SIZE) {
            createConnection();
        }
        try {
            connection = availableConnections.take();
            unavailableConnections.add(connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    private void createConnection() throws PoolException {
        try {
            availableConnections.add(ConnectionManager.getInstance().createConnection());
        } catch (SQLException e) {
            throw new PoolException("Couldn't create connection: " + e.getMessage(), e);
        }
    }

    public void releaseConnection(ProxyConnection connection) throws PoolException {
        try {
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
            unavailableConnections.remove(connection);
            availableConnections.put(connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, e);
            Thread.currentThread().interrupt();
        } catch (SQLException e) {
            throw new PoolException("Couldn't release connection: " + e.getMessage(), e);
        }
    }

    public void closeConnectionPool() throws PoolException {
        ProxyConnection connection;
        int currentPoolSize = availableConnections.size() + unavailableConnections.size();
        for (int i = 0; i < currentPoolSize; i++) {
            try {
                connection = availableConnections.take();
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
                connection.closeConnection();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                throw new PoolException("Couldn't close connection: " + e.getMessage(), e);
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        DriverManager.drivers().forEach(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Couldn't deregister driver: " + e.getMessage(), e);
            }
        });
    }

    public int getAvailableConnectionNumber(){
        return availableConnections.size();
    }

    public int getUnavailableConnectionNumber(){
        return unavailableConnections.size();
    }
}
