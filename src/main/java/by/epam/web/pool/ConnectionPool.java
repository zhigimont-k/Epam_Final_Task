package by.epam.web.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionPool {
    private static Logger logger = LogManager.getLogger();
    private static final String NUMERICAL_PATTERN = "^[1-9]\\d?$";
    private static final int INITIAL_POOL_SIZE = 8; //перенести в пропертис
    private static final int MAX_POOL_SIZE = 32; //перенести в пропертис
    private static ConnectionPool instance;
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static ReentrantLock lock = new ReentrantLock();

    private static String url;
    private static String user;
    private static String password;
    private static int poolSize;
    private BlockingQueue<ProxyConnection> availableConnections;
    private Deque<ProxyConnection> unavailableConnections;

    private ConnectionPool() {
        register();
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
        for (int i = 0; i < poolSize; i++) {
            try {
                createConnection();
            } catch (SQLException e) {
                logger.log(Level.ERROR, e);
                throw new RuntimeException(e);
            }
        }

        if (availableConnections.isEmpty()) {
            logger.fatal("Couldn't create any connections");
            throw new RuntimeException("Couldn't create any connections");
        } else if (availableConnections.size() < INITIAL_POOL_SIZE) {
            for (int i = availableConnections.size() - 1; i < poolSize; i++) {
                try {
                    createConnection();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, e);
                }
            }
        }

        if (availableConnections.size() == INITIAL_POOL_SIZE) {
            logger.log(Level.INFO, "Successfully initialized connection pool");
        }

    }

    private void register() {
        ResourceBundle bundle = ResourceBundle.getBundle(ConnectionData.BASE_NAME);
        url = bundle.getString(ConnectionData.DATABASE_URL);
        user = bundle.getString(ConnectionData.DATABASE_USER);
        password = bundle.getString(ConnectionData.DATABASE_PASSWORD);
        String poolSizeString = bundle.getString(ConnectionData.DATABASE_POOL_SIZE);
        Pattern pattern = Pattern.compile(NUMERICAL_PATTERN);
        Matcher matcher = pattern.matcher(poolSizeString);

        poolSize = (matcher.matches() && Integer.parseInt(poolSizeString) <= MAX_POOL_SIZE)
                ? Integer.parseInt(poolSizeString) : INITIAL_POOL_SIZE;

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            logger.fatal("Couldn't addUser driver", e);
            throw new RuntimeException("Couldn't addUser driver", e);
        }
    }

    public ProxyConnection getConnection() throws PoolException {
        ProxyConnection connection = null;
        if (availableConnections.size() >= INITIAL_POOL_SIZE && availableConnections.size() < MAX_POOL_SIZE) {
            createConnection();
        }
        try {
            connection = availableConnections.take();
            unavailableConnections.add(connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    private void createConnection() throws PoolException {
        try {
            ProxyConnection connection = new ProxyConnection(DriverManager.getConnection(url, user, password));
            availableConnections.add(connection);
        } catch (SQLException e) {
            throw new PoolException("Couldn't create connection", e);
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
            throw new PoolException("Couldn't release connection", e);
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
                throw new PoolException("Couldn't close connection", e);
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        DriverManager.drivers().forEach(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Couldn't deregister driver", e);
            }
        });
    }
}
