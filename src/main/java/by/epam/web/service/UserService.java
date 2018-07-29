package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.UserDao;
import by.epam.web.dao.user.impl.UserDaoImpl;
import by.epam.web.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private static final UserDao userDao = new UserDaoImpl();

    UserService() {
    }

    public User registerUser(String login, String password, String email, String phoneNumber, String userName) throws ServiceException {
        User newUser;
        try {
            newUser = new User();
            newUser.setLogin(login);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setUserName(userName);
            newUser.setPhoneNumber(phoneNumber);
            return userDao.register(newUser);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByLoginAndPassword(String login, String password) throws ServiceException {
        try {
            return userDao.findUserByLoginAndPassword(login, password);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean loginExists(String login) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.LOGIN, login);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean emailExists(String email) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.EMAIL, email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean phoneNumberExists(String phoneNumber) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.PHONE_NUMBER, phoneNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByEmail(String email) throws ServiceException {
        try {
            return userDao.findUserByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserById(int id) throws ServiceException {
        try {
            return userDao.findUserById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<User> findAllUsers() throws ServiceException {
        try {
            return userDao.findAllUsers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> changeUserStatus(String login, String status) throws ServiceException {
        try {
            return userDao.changeUserStatus(login, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> updateUser(int id, String newPassword, String newUserName) throws ServiceException {
        try {
            return userDao.updateUser(id, newPassword, newUserName);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> updateUserName(int id, String newUserName) throws ServiceException {
        try {
            return userDao.updateUserName(id, newUserName);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
