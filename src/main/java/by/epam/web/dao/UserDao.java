package by.epam.web.dao;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserDao extends AbstractDao<User> {
    enum UniqueUserInfo {
        LOGIN, EMAIL, PHONE_NUMBER
    }
    boolean propertyExists(UniqueUserInfo property, String value) throws DaoException;
    User register(User user) throws DaoException;
    Optional<User> findUserByLoginAndPassword(String login, String password) throws DaoException;
    Optional<User> findUserByLogin(String login) throws DaoException;
    Optional<User> findUserById(int id) throws DaoException;
    Optional<User> findUserByEmail(String email) throws DaoException;
    List<User> findAllUsers() throws DaoException;
    Optional<User> changeUserStatus(String login, String status) throws DaoException;
    Optional<User> updateUser(int id, String password, String userName) throws DaoException;
    Optional<User> updateUserName(int id, String userName) throws DaoException;
    Optional<User> addMoneyToCard(int userId, BigDecimal amount) throws DaoException;
    Optional<User> payForOrder(int userId, int orderId) throws DaoException;
}
