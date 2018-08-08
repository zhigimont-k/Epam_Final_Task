package by.epam.web.dao;

import by.epam.web.entity.Entity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;

public interface AbstractDao<T extends Entity> {
    Logger logger = LogManager.getLogger();

    default void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Can't close statement: " + e.getMessage(), e);
            }
        }
    }
}
