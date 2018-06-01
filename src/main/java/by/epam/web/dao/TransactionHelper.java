package by.epam.web.dao;

import by.epam.web.connection.ConnectionPool;
import by.epam.web.connection.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class TransactionHelper {
    private static Logger logger = LogManager.getLogger();
    private ProxyConnection connection = ConnectionPool.getInstance().getConnection();

    public void beginTransaction(AbstractDAO dao, AbstractDAO... daos){
        try{
            connection.setAutoCommit(false);
        } catch (SQLException e){
            logger.error(e);
        }
        dao.setConnection(connection);
        for (AbstractDAO d : daos){
            d.setConnection(connection);
        }
    }

    public void endTransaction(){
        try{
            connection.setAutoCommit(true);
        } catch (SQLException e){
            logger.error(e);
        }
    }

    public void commit(){
        try{
            connection.commit();
        } catch (SQLException e){
            logger.error(e);
        }
    }

    public void rollback(){
        try{
            connection.rollback();
        } catch (SQLException e){
            logger.error(e);
        }
    }
}
