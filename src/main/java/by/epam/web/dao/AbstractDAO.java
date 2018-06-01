package by.epam.web.dao;

import by.epam.web.connection.ProxyConnection;
import by.epam.web.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class AbstractDAO<T extends Entity> {
    protected static Logger logger = LogManager.getLogger();
    protected ProxyConnection connection;

//    public abstract List<T> findAll();
//
//    public abstract T findEntityById(int id);
//
//    public abstract boolean delete(int id);
//
//    public abstract boolean delete(T entity);
//
//    public abstract boolean create(T entity);
//
//    public abstract T update(T entity);

    protected void close(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    protected void setConnection(ProxyConnection connection){
        this.connection = connection;
    }
}
