package by.epam.web.dao;

import java.sql.SQLException;

public class DaoException extends SQLException {
    public DaoException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public DaoException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public DaoException(String reason) {
        super(reason);
    }

    public DaoException() {
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public DaoException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    public DaoException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }
}
