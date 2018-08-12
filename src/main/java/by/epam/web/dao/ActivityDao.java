package by.epam.web.dao;

import by.epam.web.entity.Activity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ActivityDao extends AbstractDao<Activity> {
    /**
     *
     * @param activity
     * activity to be added to database
     * @throws DaoException if SQLException occurs
     */
    void addActivity(Activity activity) throws DaoException;

    /**
     *
     * @param id
     * id of updated activity
     * @param name
     * new name of activity
     * @param description
     * new description of activity
     * @param price
     * new price of activity
     * @param status
     * new status of activity
     * @throws DaoException if SQLException occurs
     */
    void updateActivity(int id, String name, String description,
                                      BigDecimal price, String status) throws DaoException;

    /**
     *
     * @param id
     * id of activity to look for
     * @return
     * @throws DaoException  if SQLException occurs
     */
    Optional<Activity> findActivityById(int id) throws DaoException;

    /**
     *
     * @param name
     * name of activity to look for
     * @return
     * @throws DaoException if SQLException occurs
     */
    Optional<Activity> findActivityByName(String name) throws DaoException;

    /**
     *
     * @return
     * @throws DaoException if SQLException occurs
     */
    List<Activity> findAllActivities() throws DaoException;

    /**
     *
     * @return
     * @throws DaoException if SQLException occurs
     */
    List<Activity> findAvailableActivities() throws DaoException;
}
