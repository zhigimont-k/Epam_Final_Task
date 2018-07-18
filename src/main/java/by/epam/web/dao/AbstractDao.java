package by.epam.web.dao;

import by.epam.web.entity.Entity;

import java.sql.SQLException;
import java.sql.Statement;

public interface AbstractDao<T extends Entity> {
    default void closeStatement(Statement statement) throws DaoException{
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e){
                throw new DaoException(e);
            }
        }
    }
}
