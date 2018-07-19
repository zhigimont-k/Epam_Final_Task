package by.epam.web.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConnectionManager {
    private static final Logger logger = LogManager.getLogger();
    private static final String NUMERICAL_PATTERN = "^[1-9]\\d?$";
    static final int INITIAL_POOL_SIZE = 8;
    static final int MAX_POOL_SIZE = 32;

    private static String url;
    private static String user;
    private static String password;

    static void buildPool(){
        register();
    }

    private static void register() {
        ResourceBundle bundle = ResourceBundle.getBundle(ConnectionData.BASE_NAME);
        url = bundle.getString(ConnectionData.DATABASE_URL);
        user = bundle.getString(ConnectionData.DATABASE_USER);
        password = bundle.getString(ConnectionData.DATABASE_PASSWORD);
        String poolSizeString = bundle.getString(ConnectionData.DATABASE_POOL_SIZE);
        Pattern pattern = Pattern.compile(NUMERICAL_PATTERN);
        Matcher matcher = pattern.matcher(poolSizeString);

        int poolSize = (matcher.matches() && Integer.parseInt(poolSizeString) <= MAX_POOL_SIZE)
                ? Integer.parseInt(poolSizeString) : INITIAL_POOL_SIZE;
        ConnectionPool.setPoolSize(poolSize);

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            logger.fatal("Couldn't register driver", e);
            throw new RuntimeException("Couldn't register driver", e);
        }
    }

    static ProxyConnection createConnection() throws SQLException {
        return new ProxyConnection(DriverManager.getConnection(url, user, password));
    }

    private ConnectionManager(){}
}
