package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.UserDao;
import by.epam.web.dao.impl.UserDaoImpl;
import by.epam.web.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final UserDao userDao = new UserDaoImpl();

    UserService() {
    }

    public User registerUser(String login, String password, String email,
                             String phoneNumber, String userName, String cardNumber) throws ServiceException {
        User newUser;
        try {
            newUser = new User();
            newUser.setLogin(login);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setUserName(userName);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setCardNumber(cardNumber);
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

    public boolean cardNumberExists(String cardNumber) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.CARD_NUMBER, cardNumber);
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

    public void changeUserStatus(int userId, String status) throws ServiceException {
        try {
            userDao.changeUserStatus(userId, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void updateUser(int id, String newPassword, String newUserName) throws ServiceException {
        try {
            userDao.updateUser(id, newPassword, newUserName);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void updateUserName(int id, String newUserName) throws ServiceException {
        try {
            userDao.updateUserName(id, newUserName);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByIdAndCard(int userId, String cardNumber) throws ServiceException{
        try {
            return userDao.findUserByIdAndCardNumber(userId, cardNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void addMoneyToCard(String cardNumber, BigDecimal money) throws ServiceException {
        try {
            userDao.addMoneyToCard(cardNumber, money);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public BigDecimal findMoneyByCardNumber(String cardNumber) throws ServiceException{
        try{
            return userDao.findMoneyByCardNumber(cardNumber);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
