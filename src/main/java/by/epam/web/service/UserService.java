package by.epam.web.service;

import by.epam.web.dao.DAOException;
import by.epam.web.dao.user.impl.UserDAOImpl;
import by.epam.web.entity.User;
import by.epam.web.service.ServiceException;

public class UserService {

    public boolean registerUser(String login, String password) throws ServiceException {
        UserDAOImpl dao = new UserDAOImpl();
        User newUser;
        try {
            newUser = new User();
            newUser.setLogin(login);
            newUser.setPassword(password);
            return dao.register(newUser);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public boolean userLogin(String login, String password) throws ServiceException{
        UserDAOImpl dao = new UserDAOImpl();
        User user;
        try {
            user = new User();
            user.setLogin(login);
            user.setPassword(password);
            return dao.login(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
