package by.epam.web.dao.user;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.impl.UserDaoImpl;
import by.epam.web.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends AbstractDao<User> {
    boolean propertyExists(UserDaoImpl.UniqueUserInfo property, String value) throws DaoException;
    User register(User user) throws DaoException;
    Optional<User> findUserByLoginAndPassword(String login, String password) throws DaoException;
    Optional<User> findUserByLogin(String login) throws DaoException;
    Optional<User> findUserById(int id) throws DaoException;
    Optional<User> findUserByEmail(String email) throws DaoException;
    List<User> findAllUsers() throws DaoException;
    Optional<User> changeUserStatus(String login, String status) throws DaoException;
    Optional<User> updateUser(int id, String password, String userName) throws DaoException;
    Optional<User> updateUserName(int id, String userName) throws DaoException;
}
