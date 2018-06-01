package by.epam.web.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionPool {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool instance = null;
    private ArrayDeque<ProxyConnection> availableConnections;
    private ArrayDeque<ProxyConnection> unavailableConnections;
    private static AtomicBoolean created = new AtomicBoolean(false);
    private static ReentrantLock lock = new ReentrantLock();

    private String driver;
    private String url;
    private String user;
    private String password;
    private int poolSize;

    private static final String NUMERICAL_PATTERN = "\\d+";
    private static final int DEFAULT_POOL_SIZE = 5;

    private ConnectionPool() {
        ResourceBundle bundle = ResourceBundle.getBundle(ConnectionData.BASE_NAME);
        driver = bundle.getString(ConnectionData.DATABASE_DRIVER);
        url = bundle.getString(ConnectionData.DATABASE_URL);
        user = bundle.getString(ConnectionData.DATABASE_USER);
        password = bundle.getString(ConnectionData.DATABASE_PASSWORD);
        String poolSize = bundle.getString(ConnectionData.DATABASE_POOL_SIZE);
        Pattern pattern = Pattern.compile(NUMERICAL_PATTERN);
        Matcher matcher = pattern.matcher(poolSize);
        this.poolSize = (matcher.matches()) ? Integer.parseInt(poolSize) : DEFAULT_POOL_SIZE;
    }

    public static ConnectionPool getInstance() {
        if (!created.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    created.set(true);
                }
            } catch (Exception e) {
                logger.error(e);
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public ProxyConnection getConnection() {
        return null;
    }

    public void releaseConnection(ProxyConnection connection) {

    }
}
