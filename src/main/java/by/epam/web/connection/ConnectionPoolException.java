package by.epam.web.connection;

public class ConnectionPoolException extends RuntimeException{
    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
