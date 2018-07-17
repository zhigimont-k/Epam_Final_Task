package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.impl.UserDaoImpl;
import by.epam.web.entity.User;

import java.util.List;

public class UserService {
    private static final UserDaoImpl userDao = new UserDaoImpl();

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

    public User userLogin(String login, String password) throws ServiceException {
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

    public boolean findUserByLoginAndPassword(String login, String password) throws ServiceException {
        try {
            User found = userDao.findUserByLoginAndPassword(login, password);
            return found != null;
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

    public User changeUserStatus(String login, String status) throws ServiceException{
        try {
            return userDao.changeUserStatus(login, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
