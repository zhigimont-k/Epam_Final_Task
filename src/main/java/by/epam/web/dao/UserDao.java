package by.epam.web.dao;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserDao extends AbstractDao<User> {
    enum UniqueUserInfo {
        LOGIN, EMAIL, PHONE_NUMBER, CARD_NUMBER
    }

    /**
     * Checks if users with given prpoperty already exists
     *
     * @param property
     * The type of property to look for
     * @param value
     * Value of the property to look for
     * @return
     * Result of the check
     * @throws DaoException if exception occurs
     */
    boolean propertyExists(UniqueUserInfo property, String value) throws DaoException;

    /**
     * Adds a given user to the database
     *
     * @param user
     * User to add
     * @return
     * Added user
     * @throws DaoException if exception occurs
     */
    User register(User user) throws DaoException;

    /**
     * Looks for a user with given login and password
     *
     * @param login
     * Login to look for
     * @param password
     * Password to look for
     * @return
     * Found user
     * @throws DaoException if exception occurs
     */
    Optional<User> findUserByLoginAndPassword(String login, String password) throws DaoException;

    /**
     * Looks for a user with given ID and card number
     *
     * @param userId
     * ID to look for
     * @param cardNumber
     * Card number to look for
     * @return
     * Found user
     * @throws DaoException if exception occurs
     */
    Optional<User> findUserByIdAndCardNumber(int userId, String cardNumber) throws DaoException;

    /**
     * Looks for a user with given ID
     *
     * @param id
     * ID to look for
     * @return
     * Found user
     * @throws DaoException if exception occurs
     */
    Optional<User> findUserById(int id) throws DaoException;

    /**
     * Looks for a user with a given email
     *
     * @param email
     * Email to look for
     * @return
     * Found user
     * @throws DaoException if exception occurs
     */
    Optional<User> findUserByEmail(String email) throws DaoException;

    /**
     * Looks for all users in the database
     *
     * @return
     * List of found user
     * @throws DaoException if exception occurs
     */
    List<User> findAllUsers() throws DaoException;

    /**
     * Change user's status
     *
     * @param userId
     * ID of the user whose status to change
     * @param status
     * New user's status
     * @throws DaoException if exception occurs
     */
    void changeUserStatus(int userId, String status) throws DaoException;

    /**
     * Updates user's password and user name
     *
     * @param id
     * ID of the user to update
     * @param password
     * New password
     * @param userName
     * New user name
     * @throws DaoException if exception occurs
     */
    void updateUser(int id, String password, String userName) throws DaoException;

    /**
     * Updates user's user name
     *
     * @param id
     * ID of the user to update
     * @param userName
     * New user name
     * @throws DaoException if exception occurs
     */
    void updateUserName(int id, String userName) throws DaoException;

    /**
     * Add money to given card
     *
     * @param cardNumber
     * Number of the card to add money to
     * @param amount
     * AMount of money to add
     * @throws DaoException if exception occurs
     */
    void addMoneyToCard(String cardNumber, BigDecimal amount) throws DaoException;

    /**
     * Returns amount of money on given card
     *
     * @param cardNumber
     * Card number to look for
     * @return
     * Amount of money
     * @throws DaoException if exception occurs
     */
    BigDecimal findMoneyByCardNumber(String cardNumber) throws DaoException;
}
