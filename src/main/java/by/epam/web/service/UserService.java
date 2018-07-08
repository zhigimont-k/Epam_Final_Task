package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.user.impl.IncorrectPasswordException;
import by.epam.web.dao.user.impl.NoSuchUserException;
import by.epam.web.dao.user.impl.UserDaoImpl;
import by.epam.web.entity.User;

public class UserService {

    public User registerUser(String login, String password, String email, String phoneNumber, String userName) throws ServiceException {
        UserDaoImpl dao = new UserDaoImpl();
        User newUser;
        try {
            newUser = new User();
            newUser.setLogin(login);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setName(userName);
            newUser.setPhoneNumber(phoneNumber);
            dao.register(newUser);
            return newUser;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public User userLogin(String login, String password) throws ServiceException, NoSuchUserException, IncorrectPasswordException{
        UserDaoImpl dao = new UserDaoImpl();
        User user;
        try {
            user = new User();
            user.setLogin(login);
            user.setPassword(password);
            dao.login(user);
            return user;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean banUser(){
        return false;
    }

    //public boolean changeUserStatus(){}
}
