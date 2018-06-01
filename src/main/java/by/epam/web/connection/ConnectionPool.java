package by.epam.web.connection;

public class ConnectionPool {
    private static ConnectionPool INSTANCE;

    public static ConnectionPool getInstance(){
        return INSTANCE;
    }

    public ProxyConnection getConnection(){
        return null;
    }
}
