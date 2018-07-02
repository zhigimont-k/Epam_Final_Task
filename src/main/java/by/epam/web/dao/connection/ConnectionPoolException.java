package by.epam.web.dao.connection;

import java.sql.SQLException;

public class ConnectionPoolException extends SQLException {
    public ConnectionPoolException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public ConnectionPoolException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public ConnectionPoolException(String reason) {
        super(reason);
    }

    public ConnectionPoolException() {
    }

    public ConnectionPoolException(Throwable cause) {
        super(cause);
    }

    public ConnectionPoolException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public ConnectionPoolException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    public ConnectionPoolException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }
}
