package by.epam.web.dao.user;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.impl.UserDaoImpl;
import by.epam.web.entity.User;

public interface UserDao extends AbstractDao<User> {
    boolean propertyExists(UserDaoImpl.UniqueUserInfo property, String value) throws DaoException;
    User register(User user) throws DaoException;
}
