package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.UserDao;
import by.epam.web.dao.impl.UserDaoImpl;
import by.epam.web.entity.User;
import by.epam.web.entity.UserBuilder;
import by.epam.web.validation.NumberValidator;
import by.epam.web.validation.UserValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final UserDao userDao = new UserDaoImpl();

    UserService() {
    }

    public Optional<User> registerUser(String login, String password, String email,
                                       String phoneNumber, String userName, String cardNumber)
            throws ServiceException {
        if (!UserValidator.getInstance().validateLogin(login) ||
                !UserValidator.getInstance().validatePassword(password) ||
                !UserValidator.getInstance().validateEmail(email) ||
                !UserValidator.getInstance().validatePhoneNumber(phoneNumber) ||
                !UserValidator.getInstance().validateUserName(userName) ||
                !UserValidator.getInstance().validateCardNumber(cardNumber)) {
            return Optional.empty();
        }
        try {
            return Optional.of(userDao.register(new UserBuilder()
                    .setLogin(login)
                    .setPassword(password)
                    .setEmail(email)
                    .setUserName(userName)
                    .setPhoneNumber(phoneNumber)
                    .setCardNumber(cardNumber)
                    .create()));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByLoginAndPassword(String login, String password)
            throws ServiceException {
        try {
            return (UserValidator.getInstance().validateLogin(login) &&
                    UserValidator.getInstance().validatePassword(password)) ?
                    userDao.findUserByLoginAndPassword(login, password) :
                    Optional.empty();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean loginExists(String login) throws ServiceException {
        try {
            return UserValidator.getInstance().validateLogin(login) &&
                    userDao.propertyExists(UserDaoImpl.UniqueUserInfo.LOGIN, login);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean emailExists(String email) throws ServiceException {
        try {
            return UserValidator.getInstance().validateEmail(email) &&
                    userDao.propertyExists(UserDaoImpl.UniqueUserInfo.EMAIL, email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean phoneNumberExists(String phoneNumber) throws ServiceException {
        try {
            return UserValidator.getInstance().validatePhoneNumber(phoneNumber) &&
                    userDao.propertyExists(UserDaoImpl.UniqueUserInfo.PHONE_NUMBER, phoneNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean cardNumberExists(String cardNumber) throws ServiceException {
        try {
            return UserValidator.getInstance().validateCardNumber(cardNumber) &&
                    userDao.propertyExists(UserDaoImpl.UniqueUserInfo.CARD_NUMBER, cardNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByEmail(String email) throws ServiceException {
        try {
            return (UserValidator.getInstance().validateEmail(email)) ?
                    userDao.findUserByEmail(email) :
                    Optional.empty();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserById(String id) throws ServiceException {
        try {
            return (NumberValidator.getInstance().validateId(id)) ?
                    userDao.findUserById(Integer.parseInt(id)) :
                    Optional.empty();
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

    public boolean changeUserStatus(String userId, String status) throws ServiceException {
        if (!UserValidator.getInstance().validateStatus(status) ||
                !NumberValidator.getInstance().validateId(userId)) {
            return false;
        }
        try {
            userDao.changeUserStatus(Integer.parseInt(userId), status);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updateUser(String id, String newPassword, String newUserName)
            throws ServiceException {
        if (!UserValidator.getInstance().validatePassword(newPassword) ||
                !UserValidator.getInstance().validateUserName(newUserName) ||
                !NumberValidator.getInstance().validateId(id)) {
            return false;
        }
        try {
            userDao.updateUser(Integer.parseInt(id), newPassword, newUserName);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updateUserName(String id, String newUserName) throws ServiceException {
        if (!UserValidator.getInstance().validateUserName(newUserName) ||
                !NumberValidator.getInstance().validateId(id)) {
            return false;
        }
        try {
            userDao.updateUserName(Integer.parseInt(id), newUserName);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByIdAndCard(String userId, String cardNumber)
            throws ServiceException {
        try {
            return (UserValidator.getInstance().validateCardNumber(cardNumber) &&
                    NumberValidator.getInstance().validateId(userId)) ?
                    userDao.findUserByIdAndCardNumber(Integer.parseInt(userId), cardNumber) :
                    Optional.empty();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean addMoneyToCard(String cardNumber, String money) throws ServiceException {
        if (!UserValidator.getInstance().validateCardNumber(cardNumber) ||
                !NumberValidator.getInstance().validateMoney(money)) {
            return false;
        }
        try {
            userDao.addMoneyToCard(cardNumber, new BigDecimal(money));
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public BigDecimal findMoneyByCardNumber(String cardNumber) throws ServiceException {
        try {
            return (UserValidator.getInstance().validateCardNumber(cardNumber)) ?
                    userDao.findMoneyByCardNumber(cardNumber) :
                    BigDecimal.ZERO;

        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
